package com.onetry.spring.city.repsoitory;

import com.onetry.spring.city.entity.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CityRepository extends MongoRepository<City, String> {
    List<City> findAll();
}
