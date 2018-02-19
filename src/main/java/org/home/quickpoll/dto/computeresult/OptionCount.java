package org.home.quickpoll.dto.computeresult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class OptionCount {
    private Long optionId;
    private Integer count = 0;

    public void incrementCount() {
        count++;
    }

}
