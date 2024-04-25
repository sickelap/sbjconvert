package com.example.sbjconvert.controller;

import com.example.sbjconvert.model.ResponseEntry;
import com.example.sbjconvert.service.Converter;
import com.example.sbjconvert.validator.ValidationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/convert")
@RequiredArgsConstructor
public class ConverterController {

    private final Converter converter;
    private final ValidationConfig configuration;

    @PostMapping
    // @LogExecutionResult
    public List<ResponseEntry> convert(@RequestBody(required = false) String body) {
        return converter.convert(body, configuration.isValidationEnabled());
    }
}
