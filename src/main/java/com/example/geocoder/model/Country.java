package com.example.geocoder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
    @JsonProperty("AddressLine")
    public String addressLine;
    @JsonProperty("CountryNameCode")
    public String countryNameCode;
    @JsonProperty("CountryName")
    public String countryName;
    @JsonProperty("AdministrativeArea")
    public AdministrativeArea administrativeArea;
}
