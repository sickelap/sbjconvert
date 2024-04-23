package com.example.sbjconvert.controller;

import com.example.sbjconvert.configuration.ValidationConfiguration;
import com.example.sbjconvert.model.ResponseEntry;
import com.example.sbjconvert.service.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConverterController.class)
class ConverterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Converter converter;

    @MockBean
    private ValidationConfiguration configuration;

    @Test
    void shouldReturnInvalidRequestForNoInput() throws Exception {
        var request = post("/convert")
                .contentType(MediaType.TEXT_PLAIN)
                .content("");

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnEmptyResponseForEmptyPayload() throws Exception {
        var request = post("/convert")
                .contentType(MediaType.TEXT_PLAIN)
                .content(" ");

        mvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.size()").value(0))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReturnCorrectNumberOfRecords(boolean validationEnabled) throws Exception {
        var payload = String.join("\n", getValidRequestLines());
        var responseContent = getValidResponseEntries();
        when(configuration.isValidationEnabled()).thenReturn(validationEnabled);
        when(converter.convert(payload, validationEnabled)).thenReturn(responseContent);

        var request = post("/convert")
                .contentType(MediaType.TEXT_PLAIN)
                .content(payload);

        mvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.size()").value(getValidRequestLines().size()))
                .andExpect(content().json(objectMapper.writeValueAsString(responseContent)))
                .andDo(print());
    }

    private List<String> getValidRequestLines() {
        return List.of(
                "UUID1|ID1|name1|likes1|transport1|1.11|2.22",
                "UUID2|ID2|name2|likes2|transport2|3.33|4.44",
                "UUID3|ID3|name3|likes3|transport3|5.55|6.66"
        );
    }

    private List<ResponseEntry> getValidResponseEntries() {
        return List.of(
                new ResponseEntry("name1", "transport1", 2.22),
                new ResponseEntry("name2", "transport2", 4.44),
                new ResponseEntry("name3", "transport3", 6.66)
        );
    }
}