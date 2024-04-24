package com.example.sbjconvert.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public void handleGeolocationAccessDeniedException(HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(UnableToParseException.class)
    public void handleUnableToParsePayloadException(HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(EmptyPayloadException.class)
    public void handleEmptyPayloadException(HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }
}
