package org.home.quickpoll.controller.v1;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.home.quickpoll.domain.Poll;
import org.home.quickpoll.domain.mapper.PollMapper;
import org.home.quickpoll.dto.PollDto;
import org.home.quickpoll.dto.error.ErrorDetail;
import org.home.quickpoll.exception.ResourceNotFoundException;
import org.home.quickpoll.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(value = "polls", description = "Poll Api")
@Slf4j
public class PollController {
    private static Function<Long, Supplier<ResourceNotFoundException>> POLL_NOT_FOUND = (pollId) ->
        () -> {throw new ResourceNotFoundException("Poll with id " + pollId + " not found!" );};


    private PollService pollService;
    private PollMapper pollMapper;

    public PollController(@Autowired PollService pollService, @Autowired PollMapper pollMapper) {
        this.pollService = pollService;
        this.pollMapper = pollMapper;
    }

    @PostMapping(path = "/poll", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates a new Poll", notes="The newly created poll Id will be sent in the location response header", response = Void.class)
    @ApiResponses(value = {@ApiResponse(code=201, message="Poll Created Successfully", response=Void.class),
            @ApiResponse(code=500, message="Error creating Poll", response=ErrorDetail.class) } )
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollDto pollDto) {
        log.info("Received following data: {}",  pollDto);
        try {
            Poll poll = pollService.createPoll(pollDto);

            return ResponseEntity.created(new URI(String.format("poll/%d", poll.getId())))
                    .build();
        } catch (Exception ex) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(String.format("{\"error\": \"%s\"}", ex.getMessage()));
        }

    }

    @GetMapping(path= "/poll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return all polls", notes = "Will return all existent polls", response=Poll.class, responseContainer = "List")
    public ResponseEntity<?> getAllPolls() {

        try {
            List<Poll> allPolls = pollService.getAllPolls();
            List<PollDto> pollDtos = allPolls.stream().map(pollMapper::toPollDto).collect(Collectors.toList());
            return ResponseEntity.ok(pollDtos);

        } catch (Exception ex) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"server failed to respond on your request\"}");
        }
    }

    @GetMapping(path = "/poll/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PollDto> getPollById(@PathVariable("id") Long pollId) {
        return pollService.getPoll(pollId)
                .map(pollMapper::toPollDto)
                .map(ResponseEntity::ok)
                .orElseThrow(POLL_NOT_FOUND.apply(pollId));
    }

    @DeleteMapping(path="/poll/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePollById(@PathVariable("id") Long pollId) {
        Function<Poll, ResponseEntity<Object>> deletePoll = poll -> {
            pollService.deletePoll(pollId);
            return ResponseEntity.noContent().build();
        };

        return pollService.getPoll(pollId)
                .map(deletePoll).orElseThrow(POLL_NOT_FOUND.apply(pollId));
    }

    @PutMapping(path = "poll/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePoll(@PathVariable("id") Long pollId, @RequestBody PollDto pollDto) {
        return pollService.updatePoll(pollId, pollDto)
                .map(poll -> ResponseEntity.ok(pollMapper.toPollDto(poll)))
                .orElseThrow(POLL_NOT_FOUND.apply(pollId));
    }
}
