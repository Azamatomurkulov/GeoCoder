package com.example.geocoder;

import com.example.geocoder.dto.AddressDto;
import com.example.geocoder.dto.AddressForCorDTO;
import com.example.geocoder.entity.Address;
import com.example.geocoder.repository.AddressRepository;
import com.example.geocoder.service.AddressService;
import com.example.geocoder.service.impl.AddressServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GeoCoderTest {
    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    AddressServiceImpl addressService;

    @Test
    public void getCoordinatesTest(){
        Address cachedAddress  = new Address(1L,"бишкек",null,42.875969,74.603701, LocalDateTime.now());


            String address = "бишкек";

            List<Address> addresses = new ArrayList<>();
            addresses.add(cachedAddress );
            when(addressRepository.findByAddress(address)).thenReturn(addresses);


            AddressForCorDTO result = addressService.getCoordinates(address);


        Assert.assertEquals(result,addressService.toAddressCoordinatesDto(cachedAddress));
    }
    @Test
    public void getAddressTest() {
        Address cachedAddress  = new Address(3L,"Германия, Берлин, округ Штеглиц-Целендорф, Ортстайль Далем",
                "Берлин",52.45935663683681,
                13.253631591796877, LocalDateTime.now());


        List<Address> addresses = new ArrayList<>();
        addresses.add(cachedAddress );

        when(addressRepository.findByLatitudeAndLongitude(52.45935663683681,
                13.253631591796877))
                .thenReturn(addresses);

        AddressDto result = addressService.getAddress(52.45935663683681, 13.253631591796877);

        Assert.assertEquals(result,addressService.toAddressDto(cachedAddress));
    }
}
