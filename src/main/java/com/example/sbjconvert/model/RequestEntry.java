package com.example.sbjconvert.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

//@JsonPropertyOrder({"uuid", "id", "name", "likes", "transport", "avgSpeed", "topSpeed"})
@AllArgsConstructor
public class RequestEntry {
    public String uuid;
    public String id;
    public String name;
    public String likes;
    public String transport;
    public BigDecimal avgSpeed;
    public BigDecimal topSpeed;
}
