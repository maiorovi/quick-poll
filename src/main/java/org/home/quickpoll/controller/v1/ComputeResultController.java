package org.home.quickpoll.controller.v1;

import org.home.quickpoll.dto.computeresult.VoteResult;
import org.home.quickpoll.repository.VoteRepository;
import org.home.quickpoll.service.ComputeResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("ComputeResultControllerV1")
public class ComputeResultController {
    private ComputeResultService computeResultService;

    public ComputeResultController(@Autowired ComputeResultService computeResultService) {
        this.computeResultService = computeResultService;
    }

    @GetMapping(path = "/v1/computeresult")
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = computeResultService.getAllVotesCount(pollId);

        return ResponseEntity.ok(voteResult);
    }

}
