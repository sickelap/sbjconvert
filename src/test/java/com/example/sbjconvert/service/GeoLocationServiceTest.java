package com.example.sbjconvert.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GeoLocationServiceTest {
    private WireMockServer wireMockServer;
    private GeoLocationService classUnderTest;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        classUnderTest = new GeoLocationService();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getDetails() {
        stubFor(get("/json/1.2.3.4?fields=status,countryCode,isp").willReturn(ok()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"status\":\"success\",\"countryCode\":\"US\",\"isp\":\"Comcast\"}")));

        classUnderTest.useServer("localhost:" + wireMockServer.port());
        var response = classUnderTest.getDetails("1.2.3.4");

        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getCountryCode()).isEqualTo("US");
        assertThat(response.getIsp()).isEqualTo("Comcast");
    }
}