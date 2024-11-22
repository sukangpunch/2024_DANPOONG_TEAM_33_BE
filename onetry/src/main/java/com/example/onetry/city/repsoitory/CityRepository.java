package com.example.onetry.city.repsoitory;

import com.example.onetry.city.entity.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CityRepository extends MongoRepository<City, String> {
    List<City> findAll();
}
