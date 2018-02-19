package org.home.quickpoll.controller.advice;

import org.home.quickpoll.dto.error.ErrorDetail;
import org.home.quickpoll.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
}
