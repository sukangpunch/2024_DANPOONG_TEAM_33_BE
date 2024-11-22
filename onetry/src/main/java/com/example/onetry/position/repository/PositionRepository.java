package com.example.onetry.position.repository;

import com.example.onetry.position.entity.Position;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PositionRepository extends MongoRepository<Position, String> {
    List<Position> findAll();
}
