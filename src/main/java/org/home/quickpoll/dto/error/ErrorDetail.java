package org.home.quickpoll.dto.error;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@Data
@Builder
public class ErrorDetail {

    private String title;
    private int status;
    private String detail;
    private long timestamp;
    private String developerMesssage;
    @Setter(AccessLevel.NONE)
    private Map<String, List<ValidationError>> errors = new HashMap<>();

    public void addValidationError(String error, ValidationError validationError) {
        errors.putIfAbsent(error, newArrayList(validationError));
        errors.computeIfPresent(error, (key, value) ->  {
            value.add(validationError);
            return value;
        });
    }
}
