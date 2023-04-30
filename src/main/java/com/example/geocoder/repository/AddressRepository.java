package com.example.geocoder.repository;

import com.example.geocoder.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByLatitudeAndLongitude(Double lat,Double longt);
    List<Address> findByAddress(String address);
}
