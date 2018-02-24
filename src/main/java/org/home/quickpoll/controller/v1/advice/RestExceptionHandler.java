package org.home.quickpoll.controller.v1.advice;

import org.home.quickpoll.dto.error.ErrorDetail;
import org.home.quickpoll.dto.error.ValidationError;
import org.home.quickpoll.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    public RestExceptionHandler(@Autowired MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {
        final ErrorDetail errorDetail = ErrorDetail.builder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource Not Found")
                .detail(rnfe.getMessage())
                .developerMessage(rnfe.getClass().getName())
                .build();

        return new ResponseEntity(errorDetail, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        final Function<FieldError, ValidationError> toValidationError = error -> new ValidationError(error.getCode(), messageSource.getMessage(error, null));

        final HashMap<String, List<ValidationError>> errorMap = manve.getBindingResult().getFieldErrors()
                .stream()
                .collect(groupingBy(FieldError::getField,
                        HashMap::new,
                        mapping(toValidationError, Collectors.toList())));

        final ErrorDetail errorDetail = ErrorDetail.builder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Validation failed")
                .developerMessage(manve.getClass().getName())
                .detail("Input validation failed")
                .errors(errorMap)
                .build();

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
