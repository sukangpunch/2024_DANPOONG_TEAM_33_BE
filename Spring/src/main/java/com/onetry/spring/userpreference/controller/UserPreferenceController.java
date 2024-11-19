package com.onetry.spring.userpreference.controller;


import com.onetry.spring.common.CommonResponseDto;
import com.onetry.spring.userpreference.dto.req.AddOnboardingDataDto;
import com.onetry.spring.userpreference.dto.req.UserPreferenceUpdateDto;
import com.onetry.spring.userpreference.dto.res.UserPreferenceInfo;
import com.onetry.spring.userpreference.service.UserPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name= "[UserPreferenceController] 온보딩, 개인 수정페이지 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-preference")
public class UserPreferenceController {
    private final UserPreferenceService userPreferenceService;


    @Operation(summary = "유저의 선호 데이터 조회", description = "유저의 선호 데이터를 가져옵니다.")
    @GetMapping("/info")
    public ResponseEntity<CommonResponseDto<UserPreferenceInfo>> getUserPreference(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        UserPreferenceInfo userPreferenceInfo = userPreferenceService.getUserPreference(email);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 선호 데이터를 성공적으로 조회하였습니다.",
                userPreferenceInfo));
    }

    @Operation(summary = "온보딩 선호 데이터 생성", description = "온보딩 선호 데이터를 생성합니다.")
    @PostMapping("/onboarding")
    public ResponseEntity<CommonResponseDto<UserPreferenceInfo>> addOnboardingData(@RequestBody AddOnboardingDataDto addOnboardingDataDto,
                                                                                   @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        UserPreferenceInfo userPreferenceInfo = userPreferenceService.addOnboardingData(email, addOnboardingDataDto);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "온보딩 데이터를 추가 했습니다.",
                userPreferenceInfo
        ));
    }

    @Operation(summary = "온보딩 선호 데이터 수정", description = "온보딩 선호 데이터를 수정합니다.")
    @PatchMapping("/update")
    public ResponseEntity<CommonResponseDto<UserPreferenceInfo>> updateUserPreference(@RequestBody UserPreferenceUpdateDto userPreferenceUpdateDto,
                                                                                   @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        UserPreferenceInfo userPreferenceInfo = userPreferenceService.updateUserPreference(email, userPreferenceUpdateDto);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "온보딩 데이터를 수정 했습니다.",
                userPreferenceInfo
        ));
    }


}
