package org.home.quickpoll.dto.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetail {

    private String title;
    private int status;
    private String detail;
    private long timestamp;
    private String developerMesssage;

}
