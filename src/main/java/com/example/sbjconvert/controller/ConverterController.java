package com.example.sbjconvert.controller;

import com.example.sbjconvert.model.RequestEntry;
import com.example.sbjconvert.model.ResponseEntry;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/convert")
public class ConverterController {
    @PostMapping
    public List<ResponseEntry> convert(@RequestBody String body) throws IOException {
        var mapper = new CsvMapper();

        var schema = CsvSchema.builder()
                .addColumn("uuid")
                .addColumn("id")
                .addColumn("name")
                .addColumn("likes")
                .addColumn("transport")
                .addColumn("avgSpeed")
                .addColumn("topSpeed")
                .build()
                .withColumnSeparator('|');

        MappingIterator<RequestEntry> iterator = mapper.readerFor(RequestEntry.class)
                .with(schema)
                .readValues(body);

        return iterator.readAll().stream()
                .map(item -> new ResponseEntry(item.name, item.transport, item.topSpeed))
                .toList();
    }
}
