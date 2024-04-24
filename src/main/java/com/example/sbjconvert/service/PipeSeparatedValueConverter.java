package com.example.sbjconvert.service;

import com.example.sbjconvert.exception.ApplicationException;
import com.example.sbjconvert.exception.EmptyPayloadException;
import com.example.sbjconvert.exception.UnableToParseException;
import com.example.sbjconvert.model.RequestEntry;
import com.example.sbjconvert.model.ResponseEntry;
import com.example.sbjconvert.validator.RequestEntryValidator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class PipeSeparatedValueConverter implements Converter {

    private final RequestEntryValidator validator;

    public List<ResponseEntry> convert(String payload, boolean failOnError) throws ApplicationException {
        if (payload == null || payload.isEmpty() || payload.isBlank()) {
            throw new EmptyPayloadException();
        }
        var reader = createReader();
        var result = new ArrayList<ResponseEntry>();

        try {
            MappingIterator<RequestEntry> iterator = reader.readValues(payload);
            while (iterator.hasNextValue()) {
                var item = iterator.next();
                var errors = new BeanPropertyBindingResult(item, "requestEntry");
                validator.validate(item, errors);
                if (errors.hasErrors()) {
                    throw new RuntimeException();
                }
                result.add(new ResponseEntry(item.name(), item.transport(), item.topSpeed()));
            }
        } catch (Exception e) {
            if (failOnError) {
                throw new UnableToParseException();
            }
        }

        return result;
    }

    private ObjectReader createReader() {
        var mapper = new CsvMapper();
        var schema = CsvSchema.builder()
                .addColumn("uuid", CsvSchema.ColumnType.STRING)
                .addColumn("id", CsvSchema.ColumnType.STRING)
                .addColumn("name", CsvSchema.ColumnType.STRING)
                .addColumn("likes", CsvSchema.ColumnType.STRING)
                .addColumn("transport", CsvSchema.ColumnType.STRING)
                .addColumn("avgSpeed", CsvSchema.ColumnType.NUMBER)
                .addColumn("topSpeed", CsvSchema.ColumnType.NUMBER)
                .build()
                .withColumnSeparator('|');

        return mapper.readerFor(RequestEntry.class)
                .with(schema);
    }
}
