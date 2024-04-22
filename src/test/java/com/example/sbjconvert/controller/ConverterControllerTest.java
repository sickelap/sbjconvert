package com.example.sbjconvert.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ConverterController.class)
class ConverterControllerTest {

    @Autowired
    private MockMvc mvc;

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
}