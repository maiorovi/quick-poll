package org.home.quickpoll.repository;

import org.home.quickpoll.domain.Poll;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long> {

    List<Poll> findAll();
}
