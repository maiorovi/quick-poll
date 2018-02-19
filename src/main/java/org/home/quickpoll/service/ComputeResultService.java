package org.home.quickpoll.service;

import org.home.quickpoll.domain.Vote;
import org.home.quickpoll.dto.computeresult.OptionCount;
import org.home.quickpoll.dto.computeresult.VoteResult;
import org.home.quickpoll.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComputeResultService {

    private VoteRepository voteRepository;

    public ComputeResultService(@Autowired  VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public VoteResult getAllVotesCount(Long pollId) {
        List<Vote> votes = voteRepository.findByPoll(pollId);
        Map<Long, OptionCount> count = new HashMap<>();

        for (Vote vote : votes) {
            Long optionId = vote.getOptionId();

            count.computeIfPresent(optionId, (optId, optCount) -> {
                optCount.incrementCount();
                return optCount;
            });

            count.putIfAbsent(optionId, new OptionCount(optionId, 1));
        }

        int votesCount = count.values().stream().mapToInt(OptionCount::getCount).sum();

        VoteResult voteResult = new VoteResult();
        voteResult.setResults(count.values());
        voteResult.setTotalVotes(votesCount);

        return voteResult;
    }
}
