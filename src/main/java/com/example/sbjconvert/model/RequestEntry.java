package com.example.sbjconvert.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"uuid", "id", "name", "likes", "transport", "avgSpeed", "topSpeed"})
public record RequestEntry(String uuid, String id, String name, String likes, String transport, Double avgSpeed,
                           Double topSpeed) {
}
