package org.home.quickpoll.domain.mapper;

import org.home.quickpoll.domain.Option;
import org.home.quickpoll.domain.Vote;
import org.home.quickpoll.dto.OptionDto;
import org.home.quickpoll.dto.VoteDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    VoteDto toVoteDto(Vote vote);
    Option toOption(OptionDto optionDto);

    Vote toVote(VoteDto voteDto);
    OptionDto toOptionDto(Option option);
}
