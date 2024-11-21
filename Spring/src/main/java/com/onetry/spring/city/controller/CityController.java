package com.onetry.spring.city.controller;


import com.onetry.spring.city.entity.City;
import com.onetry.spring.city.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "[City] 온보딩, 유저 선호 데이터 추가에서 지역 정보들을 가져오는 API 입니다. 서울 - 중구, 종로구, 노원구 형식으로 리턴")
@RestController
@RequiredArgsConstructor

public class CityController {

    private final CityService cityService;

    @Operation(summary = "지역 정보를 가져옵니다." ,description = "지역 정보를 가져옵니다 서울 - 중구, 종로구 노원구")
    @GetMapping("/all")
    public ResponseEntity<List<City>> getCityData(){
        return ResponseEntity.status(HttpStatus.OK).body(cityService.getCityData());
    }

}
