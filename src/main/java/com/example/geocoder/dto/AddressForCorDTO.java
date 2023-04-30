package com.example.geocoder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressForCorDTO {
    private String address;
    private Double latitude;
    private Double longitude;
}
