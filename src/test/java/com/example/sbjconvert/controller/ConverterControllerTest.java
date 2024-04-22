package com.example.sbjconvert.controller;

import com.example.sbjconvert.model.ResponseEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ConverterController.class)
class ConverterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void shouldReturnCorrectNumberOfRecords() throws Exception {
        var request = post("/convert")
                .contentType(MediaType.TEXT_PLAIN)
                .content(String.join("\n", getValidRequestLines()));

        mvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.size()").value(getValidRequestLines().size()))
                .andExpect(content().json(objectMapper.writeValueAsString(getValidResponseEntries())))
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
                new ResponseEntry("name1", "transport1", BigDecimal.valueOf(2.22)),
                new ResponseEntry("name2", "transport2", BigDecimal.valueOf(4.44)),
                new ResponseEntry("name3", "transport3", BigDecimal.valueOf(6.66))
        );
    }
}