package com.example.sbjconvert.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

public record ResponseEntry(String name, String transport, BigDecimal topSpeed) {
}
