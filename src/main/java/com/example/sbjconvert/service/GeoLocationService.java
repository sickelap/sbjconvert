package com.example.sbjconvert.service;

import com.example.sbjconvert.model.GeoLocationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class GeoLocationService {
    private static final String API_URL_TEMPLATE = "http://{host}/json/{ip}?fields={fields}";
    private static final String DEFAULT_API_HOST = "ip-api.com";
    private static final String DEFAULT_API_FIELDS = "status,countryCode,isp";
    private String host = DEFAULT_API_HOST;

    public GeoLocationResponse getDetails(String ip) {
        log.info("Fetching GeoLocation details for IP: {}", ip);

        try {
            var uri = new URI(API_URL_TEMPLATE.replace("{host}", host)
                    .replace("{ip}", ip)
                    .replace("{fields}", DEFAULT_API_FIELDS)
            );
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), GeoLocationResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching GeoLocation details", e);
        }
    }

    public void useServer(String host) {
        this.host = host;
    }
}