package com.example.sbjconvert.exception;

public class GeolocationAccessDeniedException extends RuntimeException {
    public GeolocationAccessDeniedException() {
        super("Geolocation access denied");
    }
}
