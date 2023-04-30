package com.example.geocoder.controller;

import com.example.geocoder.dto.AddressDto;
import com.example.geocoder.dto.AddressForCorDTO;
import com.example.geocoder.service.AddressService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class GeoController {
    AddressService addressService;


    @GetMapping("/address")
    public AddressForCorDTO getCoordinates(@RequestParam("address") String address) {
        return addressService.getCoordinates(address);
    }

    @GetMapping("/coordinates")
    public AddressDto getAddress(@RequestParam("lat") Double latitude,
                                 @RequestParam("long") Double longitude){
        return addressService.getAddress(latitude,longitude);
    }

}
