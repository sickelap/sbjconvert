package com.example.sbjconvert.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(GeolocationAccessDeniedException.class)
    public ResponseEntity<Object> handleGeolocationAccessDeniedException(GeolocationAccessDeniedException ex) {
        var apiError = new ApplicationError(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnableToParseException.class)
    public ResponseEntity<Object> handleUnableToParsePayloadException(UnableToParseException ex) {
        var apiError = new ApplicationError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        var apiError = new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
