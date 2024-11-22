package com.example.onetry.apply.controller;

import com.example.onetry.apply.dto.ApplicationInfo;
import com.example.onetry.apply.service.ApplicationService;
import com.example.onetry.common.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="[Application] 지원서 를 조회, 삭제 합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
@Log4j2
public class ApplicationController {
    private final ApplicationService applicationService;
    @Operation(summary = "지원서 조회", description = "유저가 지원하거나 저장한 지원서 정보를 정렬 기준에 따라 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<CommonResponseDto<List<ApplicationInfo>>> getAllApplications(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "sortBy", defaultValue = "recent_desc") String sortBy) { // 정렬 기준 추가
        String email = userDetails.getUsername();
        List<ApplicationInfo> applicationInfos = applicationService.getAllApplications(email, sortBy);
        log.info("컨트롤러 첫 번쨰 데이터 : {}", applicationInfos.get(0).title());
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto(
                "성공적으로 지원서 조회를 완료했습니다.",
                applicationInfos
        ));
    }
    @Operation(summary = "지원서 삭제", description = "저장, 지원 한 지원서를 삭제합니다.")
    @DeleteMapping("/delete/{applicationId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteApplication(
            @PathVariable("applicationId") Long id,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        applicationService.deleteApplication(email, id);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "성공적으로 지원서 삭제를 완료하였습니다.",
                null
        ));
    }
}
