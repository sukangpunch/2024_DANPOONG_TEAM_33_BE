package com.onetry.spring.certification.controller;


import com.onetry.spring.certification.dto.req.CertificationCreateDto;
import com.onetry.spring.certification.dto.res.CertificationInfo;
import com.onetry.spring.certification.service.CertificationService;
import com.onetry.spring.common.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[Certification] Certification API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/certification")
@Log4j2
public class CertificationController {

    private final CertificationService certificationService;

    @Operation(summary = "유저의 자격증 전부 조회", description = "유저의 모든 자격증을 가져옵니다.")
    @GetMapping("/all")
    public ResponseEntity<CommonResponseDto<List<CertificationInfo>>> getAllCertification(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        List<CertificationInfo> certificationDetails = certificationService.getAllCertification(email);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 모든 자격증을 성공적으로 조회하였습니다.",
                certificationDetails));
    }

    @Operation(summary = "자격증을 추가합니다.", description = "인증된 유저의 자격증을 추가합니다.")
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto<CertificationInfo>> addCertification(@ModelAttribute CertificationCreateDto certificationCreateDto,
                                                                                 @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        CertificationInfo certificationDetail = certificationService.addCertification(email, certificationCreateDto);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 자격증을 성공적으로 추가 하였습니다.",
                certificationDetail
        ));
    }

    @Operation(summary = "자격증 삭제", description = "자격증을 삭제합니다.")
    @DeleteMapping("/delete/{certificationId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteCertification(@PathVariable("certificationId") Long certificationId,
                                                                       @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        certificationService.deleteCertification(certificationId, email);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        certificationId + "번 자격증이 삭제되었습니다",
                        null
                )
        );
    }

}
