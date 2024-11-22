package com.example.onetry.city.service;

import com.example.onetry.city.entity.City;
import com.example.onetry.city.repsoitory.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    public List<City> getCityData() {
        List<City> cities = cityRepository.findAll();
        return cities;
    }
}
