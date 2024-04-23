package com.example.sbjconvert.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ApplicationError {
    private HttpStatus status;
    private String message;

    public ApplicationError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
