package com.example.geocoder.service;

import com.example.geocoder.dto.AddressDto;
import com.example.geocoder.dto.AddressForCorDTO;
import org.springframework.stereotype.Component;

@Component
public interface AddressService {
    AddressDto getAddress(Double lat,Double longt);
    AddressForCorDTO getCoordinates(String address);
}
