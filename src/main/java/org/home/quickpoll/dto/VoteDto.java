package org.home.quickpoll.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Data
public class VoteDto {
    private Long id;
    private OptionDto option;
}
