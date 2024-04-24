package com.example.sbjconvert.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GeoLocationStatus {
    @JsonProperty("success")
    SUCCESS,
    @JsonProperty("fail")
    FAIL
}
