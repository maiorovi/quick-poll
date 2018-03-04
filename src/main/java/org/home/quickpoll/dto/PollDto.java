package org.home.quickpoll.dto;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollDto extends ResourceSupport {
    private long pollId;
    @NotEmpty
    private String question;
    @Singular
    @Size(min=2, max=6)
    private Set<OptionDto> options;
}
