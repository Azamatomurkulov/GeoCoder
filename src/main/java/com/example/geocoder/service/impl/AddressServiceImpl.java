package com.example.geocoder.service.impl;

import com.example.geocoder.dto.AddressDto;
import com.example.geocoder.dto.AddressForCorDTO;
import com.example.geocoder.entity.Address;
import com.example.geocoder.model.AddressDetails;
import com.example.geocoder.model.Pos;
import com.example.geocoder.repository.AddressRepository;
import com.example.geocoder.service.AddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    AddressRepository addressRepository;

    private final String APIKEY = "b8ffb334-f70d-4e91-bfba-3be088d56bd8";


    @Override
    public AddressDto getAddress(Double lat, Double longt) {
        log.info("getAddress: Начало работы метода поиска адреса по координатам.");
        List<Address> addresses = addressRepository.findByLatitudeAndLongitude(lat,longt);
        log.info("Поиск введенных координат в репозитории.");

        Duration duration = Duration.ofMinutes(30);

        if(addresses!=null){
            for(Address address3: addresses){
                if(Duration.between(address3.getCreatedTime(), LocalDateTime.now()).compareTo(duration) <= 0){
                    if(Math.abs(address3.getLatitude() - lat) <=0.00001 && Math.abs(address3.getLongitude() - longt) <=0.00001){
                        log.info("Координаты найдены и соответсвуют запросу, данные будут отправленны из репозитория.");
                        return toAddressDto(address3);
                    }

                }
            }}



        JsonMapper jsonMapper = new JsonMapper();
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        AddressDetails addressDetails2;

        String url = "https://geocode-maps.yandex.ru/1.x/?format=json&apikey="+APIKEY+"&geocode="+longt+" "+lat;


        String json =restTemplate.getForObject(url, String.class);
        log.info("Был произведен запрос по Апи и получен ответ ввиде JSON и преобразован в String.");


        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(json);
            log.info("Полученный ответ был преобразован в структуру дерева JSON.");
        } catch (JsonProcessingException e) {
            log.error("Произошла ошибка" + e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("Начало парсинга JSON дерева, и выборка нужного блока.");
        JsonNode response = rootNode.get("response");
        JsonNode geoObjectCollection = response.get("GeoObjectCollection");
        JsonNode featureMember = geoObjectCollection.get("featureMember");
        JsonNode geoObject = featureMember.get(0);
        JsonNode geoObject2 = geoObject.get("GeoObject");
        JsonNode metaData = geoObject2.get("metaDataProperty");
        JsonNode geocoderMetaData = metaData.get("GeocoderMetaData");
        JsonNode addressDetails = geocoderMetaData.get("AddressDetails");


        try {
            addressDetails2 = jsonMapper.treeToValue(addressDetails, AddressDetails.class);
            log.info("Маппинг выбранного блока в обьект Java.");
        } catch (JsonProcessingException e) {
            log.error("Произошла ошибка" + e.getMessage());
            throw new RuntimeException(e);

        }
        Address address = toEntity(addressDetails2);
        address.setLatitude(lat);
        address.setLongitude(longt);
        address.setCreatedTime(LocalDateTime.now());

        return toAddressDto(addressRepository.save(address));
    }
    @Override
    public AddressForCorDTO getCoordinates(String address){
        log.info("getCoordinates: начало поиска координат по адресу.");
        List<Address> addresses = addressRepository.findByAddress(address);
        Duration duration = Duration.ofMinutes(30);

        if(addresses!=null){
        for(Address address3: addresses){
            if(Duration.between(address3.getCreatedTime(), LocalDateTime.now()).compareTo(duration) <= 0){
                if(address3.getAddress().equals(address)){
                    log.info("Адрес найден в базе данных и будет отправлен как результат.");
                    return toAddressCoordinatesDto(address3);
                }

            }
        }}


        JsonMapper jsonMapper = new JsonMapper();
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        Pos pos;


        String url = "https://geocode-maps.yandex.ru/1.x/?format=json&apikey="+APIKEY+"&geocode="+address;


        String json =restTemplate.getForObject(url, String.class);
        log.info("Получен JSON ответ с сервера и преобразован в строку.");


        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("Произошла ошибка" + e.getMessage());
            throw new RuntimeException(e);
        }
        JsonNode response = rootNode.get("response");
        JsonNode geoObjectCollection = response.get("GeoObjectCollection");
        JsonNode featureMember = geoObjectCollection.get("featureMember");
        JsonNode geoObject = featureMember.get(0);
        JsonNode geoObject2 = geoObject.get("GeoObject");
        JsonNode point2 = geoObject2.get("Point");


        try {
            pos = jsonMapper.treeToValue(point2, Pos.class);

        } catch (JsonProcessingException e) {
            log.error("Произошла ошибка" + e.getMessage());
            throw new RuntimeException(e);
        }
        Address address2 = toAddressCoordinates(pos);
        address2.setAddress(address);
        address2.setCreatedTime(LocalDateTime.now());
        log.info("Завершение метода по поиску адреса.");

        return toAddressCoordinatesDto(addressRepository.save(address2));
    }
    public Address toAddressCoordinates(Pos pos){
        Address address = new Address();
        String[] strings = pos.getPos().split(" ");
        address.setLongitude(Double.parseDouble(strings[0]));
        address.setLatitude(Double.parseDouble(strings[1]));
        return address;
    }
    public AddressForCorDTO toAddressCoordinatesDto(Address address){
        return new AddressForCorDTO(address.getAddress(),
                address.getLatitude(),
                address.getLongitude());
    }

    public Address toEntity(AddressDetails addressDetails){
        Address address = new Address();
        address.setAddress(addressDetails.getCountry().getAddressLine());
        address.setAdministrativeArea(addressDetails.getCountry().getAdministrativeArea().getAdministrativeAreaName());
        return address;
    }

    public AddressDto toAddressDto(Address address){
        return new AddressDto(address.getAddress(),
                address.getAdministrativeArea());
    }
}
