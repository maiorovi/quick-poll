package org.home.quickpoll.repository;

import org.home.quickpoll.domain.Poll;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PollRepository extends CrudRepository<Poll, Long>{

    List<Poll> findAll();
}
