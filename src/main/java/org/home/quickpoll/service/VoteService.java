package org.home.quickpoll.service;

import org.home.quickpoll.domain.Vote;
import org.home.quickpoll.domain.mapper.VoteMapper;
import org.home.quickpoll.dto.VoteDto;
import org.home.quickpoll.repository.PollRepository;
import org.home.quickpoll.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private VoteRepository voteRepository;
    private PollRepository pollRepository;
    private VoteMapper voteMapper;

    public VoteService(@Autowired VoteRepository voteRepository, @Autowired VoteMapper voteMapper,@Autowired PollRepository pollRepository) {
        this.voteMapper = voteMapper;
        this.voteRepository = voteRepository;
        this.pollRepository = pollRepository;
    }

    public Vote createVote(VoteDto voteDto) {
        return voteRepository.save(voteMapper.toVote(voteDto));
    }

    public List<VoteDto> getAllVotes(Long pollId) {
        return voteRepository.findByPoll(pollId)
                .stream().map(voteMapper::toVoteDto)
                .collect(Collectors.toList());
    }

    public boolean pollExists(Long pollId) {
        return pollRepository.exists(pollId);
    }
}
