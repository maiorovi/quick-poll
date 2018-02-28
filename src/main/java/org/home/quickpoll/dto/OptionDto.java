package org.home.quickpoll.dto;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OptionDto {
    @NonNull
    private String value;
    private Long id;
}
