package com.onetry.spring.position.Service;


import com.onetry.spring.position.entity.Position;
import com.onetry.spring.position.repository.PositionRepository;
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
