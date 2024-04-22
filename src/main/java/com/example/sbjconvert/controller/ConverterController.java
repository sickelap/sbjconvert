package com.example.sbjconvert.controller;

import com.example.sbjconvert.model.ResponseEntry;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/convert")
public class ConverterController {
    @PostMapping
    public List<ResponseEntry> convert(@RequestBody String body) {
        return List.of();
    }
}
