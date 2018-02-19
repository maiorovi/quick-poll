package org.home.quickpoll.domain.mapper;

import org.home.quickpoll.domain.Option;
import org.home.quickpoll.domain.Poll;
import org.home.quickpoll.dto.OptionDto;
import org.home.quickpoll.dto.PollDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, componentModel = "spring")
public interface PollMapper {

    Poll toPoll(PollDto pollDto);
    Option toOption(OptionDto optionDto);

    PollDto toPollDto(Poll poll);
    OptionDto toOptionDto(Option option);
}
