package org.home.quickpoll.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private Long id;
    private OptionDto option;
}
