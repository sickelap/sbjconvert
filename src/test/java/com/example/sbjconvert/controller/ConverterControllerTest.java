package com.example.sbjconvert.controller;

import com.example.sbjconvert.service.Converter;
import com.example.sbjconvert.validator.ValidationConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConverterControllerTest {

    @Mock
    private Converter converter;

    @Mock
    private ValidationConfig configuration;

    private ConverterController classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new ConverterController(converter, configuration);
    }

    @Test
    void shouldReturnEmptyResponseForEmptyPayload() {
        var payload = "a|b|c|d|e|1|2";
        when(configuration.isValidationEnabled()).thenReturn(false);
        when(converter.convert(payload, false)).thenReturn(null);

        var result = classUnderTest.convert(payload);

        Assertions.assertNull(result);
        verify(converter).convert(payload, false);
    }
}