package org.home.quickpoll.repository;

import org.home.quickpoll.domain.Vote;
import org.home.quickpoll.dto.VoteDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    @Query(value = "select v.* from opt o, vote v where v.option_id = o.option_id and o.poll_id = ?1", nativeQuery = true)
    List<Vote> findByPoll(Long pollId);
}
