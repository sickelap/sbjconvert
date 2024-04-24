package com.example.sbjconvert.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Getter
public class GeoLocationConfiguration {
    @Value("${geo.block.enabled}")
    private boolean enabled;

    @Value("${geo.block.countries}")
    private List<String> blockedCountries;

    @Value("${geo.block.providers}")
    private List<String> blockedProviders;

    @Value("${geo.allow.ips}")
    private List<String> allowedIps;
}