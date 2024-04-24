package com.example.sbjconvert.exception;

public class EmptyPayloadException extends ApplicationException {
    public EmptyPayloadException() {
        super("Empty payload");
    }
}
