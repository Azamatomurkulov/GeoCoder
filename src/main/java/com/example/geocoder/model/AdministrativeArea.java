package com.example.geocoder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdministrativeArea {
    @JsonProperty("AdministrativeAreaName")
    public String administrativeAreaName;
    @JsonProperty("Locality")
    public Locality locality;
}
