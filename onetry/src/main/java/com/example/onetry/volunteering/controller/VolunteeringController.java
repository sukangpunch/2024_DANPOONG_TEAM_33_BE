package com.example.onetry.volunteering.controller;

import com.example.onetry.common.CommonResponseDto;
import com.example.onetry.volunteering.dto.VolunteeringCreateDto;
import com.example.onetry.volunteering.dto.VolunteeringInfo;
import com.example.onetry.volunteering.service.VolunteeringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[Volunteering] 봉사활동 정보를 저장, 조회, 삭제 합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/volunteering")
public class VolunteeringController {
    private final VolunteeringService volunteeringService;

    @Operation(summary = "유저의 봉사활동 전부 조회", description = "유저의 모든 봉사활동 가져옵니다.")
    @GetMapping("/all")
    public ResponseEntity<CommonResponseDto<List<VolunteeringInfo>>> getAllVolunteering(
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        List<VolunteeringInfo> volunteeringInfos = volunteeringService.getAllVolunteering(email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 모든 봉사활동을 성공적으로 조회하였습니다.",
                volunteeringInfos
        ));
    }

    @Operation(summary = "봉사활동을 추가합니다.", description = "인증된 유저의 봉사활동 추가합니다.")
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto<VolunteeringInfo>> addTranscript(
            @ModelAttribute VolunteeringCreateDto volunteeringCreateDto,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        VolunteeringInfo volunteeringInfo = volunteeringService.addVolunteering(email, volunteeringCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 봉사활동을 성공적으로 추가 하였습니다.",
                volunteeringInfo
        ));
    }

    @Operation(summary = "봉사활동 삭제", description = "봉사활동을 삭제합니다.")
    @DeleteMapping("/delete/{volunteeringId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteVolunteering(
            @PathVariable("volunteeringId") Long volunteeringId,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        volunteeringService.deleteVolunteering(volunteeringId, email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                        volunteeringId + "번 봉사활동이 삭제되었습니다",
                        null
        ));
    }

}
