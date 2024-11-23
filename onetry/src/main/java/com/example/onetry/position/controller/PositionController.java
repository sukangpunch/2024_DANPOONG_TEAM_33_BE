package com.example.onetry.position.controller;

import com.example.onetry.position.Service.PositionService;
import com.example.onetry.position.entity.Position;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "[Position] 온보딩, 유저 선호 데이터 수정에서 업종 데이터 가져올 떄 사용할  API")
@RestController
@RequestMapping("/api/position")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @Operation(summary = "업종 데이터 가져오기", description = "업종 데이터들을 가져옵니다. 기획-전략 -> 프로덕트 매니저, 경영 컨설턴트 등등 리스트 구조")
    @GetMapping("/all")
    public ResponseEntity<List<Position>> getPositionData()
    {
        return ResponseEntity.status(HttpStatus.OK).body(positionService.getAllPosition());
    }
}
