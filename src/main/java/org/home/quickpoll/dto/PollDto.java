package org.home.quickpoll.dto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollDto {
    private String question;
    @Singular
    private Set<OptionDto> options;
}
