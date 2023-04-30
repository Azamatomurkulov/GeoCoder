package com.example.geocoder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Locality {
    @JsonProperty("LocalityName")
    public String localityName;
    @JsonProperty("Thoroughfare")
    public Thoroughfare thoroughfare;
}
