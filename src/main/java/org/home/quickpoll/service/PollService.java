package org.home.quickpoll.service;

import lombok.extern.slf4j.Slf4j;
import org.home.quickpoll.domain.Poll;
import org.home.quickpoll.domain.mapper.PollMapper;
import org.home.quickpoll.dto.PollDto;
import org.home.quickpoll.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional
public class PollService {

    private PollRepository pollRepository;
    private PollMapper pollMapper;

    public PollService(@Autowired PollRepository pollRepository,@Autowired PollMapper pollMapper) {
        this.pollRepository = pollRepository;
        this.pollMapper = pollMapper;
    }

    public Poll createPoll(PollDto pollDto) {
        Poll poll = pollMapper.toPoll(pollDto);

        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls() {

        List<Poll> polls = pollRepository.findAll();

        return polls;
    }

    public Page<PollDto> getAllPolls(Pageable pageable) {

        Page<Poll> page = pollRepository.findAll(pageable);

        return page.map(poll -> pollMapper.toPollDto(poll));
    }

    public Optional<Poll> getPoll(Long pollId) {
        return Optional.ofNullable(pollRepository.findOne(pollId));
    }

    public void deletePoll(Long pollId) {
        pollRepository.delete(pollId);
    }

    public Optional<Poll> updatePoll(Long pollId, PollDto pollDto) {
        return getPoll(pollId).map( poll -> {
            final Poll updatedPoll = pollMapper.toPoll(pollDto);
            poll.setQuestion(updatedPoll.getQuestion());
            poll.removeAllOptions();
            poll.addAllOptions(updatedPoll.getOptions());

            return pollRepository.save(poll);
        });
    }
}
