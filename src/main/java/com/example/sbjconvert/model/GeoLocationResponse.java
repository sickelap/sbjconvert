package com.example.sbjconvert.model;

import lombok.Data;

@Data
public class GeoLocationResponse {
    private String status;
    private String countryCode;
    private String isp;
}
