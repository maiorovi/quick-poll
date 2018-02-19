package org.home.quickpoll.controller.advice;

import org.home.quickpoll.dto.error.ErrorDetail;
import org.home.quickpoll.dto.error.ValidationError;
import org.home.quickpoll.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {
        final ErrorDetail errorDetail = ErrorDetail.builder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource Not Found")
                .detail(rnfe.getMessage())
                .developerMesssage(rnfe.getClass().getName())
                .build();

        return new ResponseEntity(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request) {
        final Function<FieldError, ValidationError> toValidationError = error -> new ValidationError(error.getCode(), error.getDefaultMessage());

        final HashMap<String, List<ValidationError>> errorMap = manve.getBindingResult().getFieldErrors()
                .stream()
                .collect(groupingBy(error -> error.getField(),
                        HashMap::new,
                        mapping(toValidationError, Collectors.toList())));

        final ErrorDetail errorDetail = ErrorDetail.builder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Validation failed")
                .developerMesssage(manve.getClass().getName())
                .detail("Input validation failed")
                .errors(errorMap)
                .build();

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
