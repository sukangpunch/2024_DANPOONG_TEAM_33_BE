package com.example.onetry.position.Service;

import com.example.onetry.position.entity.Position;
import com.example.onetry.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;
    public List<Position> getAllPosition(){
        return positionRepository.findAll();
    }

}
