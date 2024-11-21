package com.onetry.spring.position.repository;

import com.onetry.spring.position.entity.Position;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PositionRepository extends MongoRepository<Position, String> {
    List<Position> findAll();
}
