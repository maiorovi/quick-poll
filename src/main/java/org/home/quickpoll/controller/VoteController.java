package org.home.quickpoll.controller;

import com.wordnik.swagger.annotations.ApiOperation;
import org.home.quickpoll.domain.Vote;
import org.home.quickpoll.domain.mapper.VoteMapper;
import org.home.quickpoll.dto.VoteDto;
import org.home.quickpoll.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class VoteController {

    private VoteService voteService;
    private VoteMapper voteMapper;

    public VoteController(@Autowired VoteService voteService, @Autowired VoteMapper voteMapper) {
        this.voteService = voteService;
        this.voteMapper = voteMapper;
    }

    @PostMapping(path = "/poll/{pollId}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "creates a vote for some option of a poll", response = Void.class)
    public ResponseEntity<?> createVote(@RequestBody VoteDto voteDto, @PathVariable("pollId") Long pollId) throws URISyntaxException {
        final Vote createdVote = voteService.createVote(voteDto);

        String str = String.format("/polls/%d/votes/%d",pollId, createdVote.getId());
        final URI location = new URI(str);

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/poll/{pollId}/votes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "creates a vote for some option of a poll", response = VoteDto.class, responseContainer = "List")
    public ResponseEntity<?> getAllVotes(@PathVariable("pollId") Long pollId) {
        return ResponseEntity.ok(voteService.getAllVotes(pollId));
    }

}
