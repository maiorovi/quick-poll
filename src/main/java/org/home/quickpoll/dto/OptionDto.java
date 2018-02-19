package org.home.quickpoll.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OptionDto {
    @NonNull
    private String value;
    private Long id;
}
