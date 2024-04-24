package com.example.sbjconvert.exception;

public class AccessDeniedException extends ApplicationException {
    public AccessDeniedException() {
        super("Geolocation access denied");
    }
}
