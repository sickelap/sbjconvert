package com.example.sbjconvert.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "request_log")
@Data
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uri;
    private Timestamp timestamp;
    private int httpResponseCode;
    private String ip;
    private String countryCode;
    private String provider;
    private double timeLapsed;
}
