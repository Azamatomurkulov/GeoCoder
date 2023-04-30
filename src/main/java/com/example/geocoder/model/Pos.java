package com.example.geocoder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Pos {

    @JsonProperty("pos")
    public String pos;
}
