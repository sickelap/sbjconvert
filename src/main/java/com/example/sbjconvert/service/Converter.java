package com.example.sbjconvert.service;

import com.example.sbjconvert.model.ResponseEntry;

import java.util.List;

public interface Converter {
    List<ResponseEntry> convert(String payload, boolean failOnError) throws RuntimeException;
}
