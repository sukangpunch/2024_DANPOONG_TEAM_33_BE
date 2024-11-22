package com.example.onetry.certification.controller;

import com.example.onetry.certification.dto.req.CertificationCreateDto;
import com.example.onetry.certification.dto.res.CertificationInfo;
import com.example.onetry.certification.service.CertificationService;
import com.example.onetry.common.CommonResponseDto;
import com.example.onetry.common.FileDownload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "[Certification] 자격증을 조회, 저장, 삭제합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/certification")
@Log4j2
public class CertificationController {

    private final CertificationService certificationService;

    @Operation(summary = "자격증 상세 정보 조회", description = "certificationId 에 해당하는 게시물을 조회")
    @GetMapping("/{certificationId}")
    public ResponseEntity<CommonResponseDto<CertificationInfo>> getCertificationDetail(
            @PathVariable("certificationId") Long certificationId,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        CertificationInfo certificationDetail= certificationService.getCertification(certificationId, email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "자격증 상세 정보를 성공적으로 조회하였습니다.",
                certificationDetail
        ));
    }

    @Operation(summary = "유저의 자격증 전부 조회", description = "유저의 모든 자격증을 가져옵니다.")
    @GetMapping("/all")
    public ResponseEntity<CommonResponseDto<List<CertificationInfo>>> getAllCertification(
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        List<CertificationInfo> certificationDetails = certificationService.getAllCertification(email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 모든 자격증을 성공적으로 조회하였습니다.",
                certificationDetails
        ));
    }

    @Operation(summary = "자격증을 추가합니다.", description = "인증된 유저의 자격증을 추가합니다.")
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto<CertificationInfo>> addCertification(
            @ModelAttribute CertificationCreateDto certificationCreateDto,
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
    public ResponseEntity<CommonResponseDto<Void>> deleteCertification(
            @PathVariable("certificationId") Long certificationId,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        certificationService.deleteCertification(certificationId, email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                        certificationId + "번 자격증이 삭제되었습니다",
                        null
        ));
    }

    @Operation(summary = "자격증 다운로드",description = "유저의 자격증을 다운로드합니다.")
    @GetMapping("/download")
    public ResponseEntity<Resource> findCertificationFile(
            final @RequestParam("certificationName") String certificationName){
        FileDownload certificationDownload = certificationService.downloadCertificationFromFileSystem(certificationName);
        String encodedFileName = UriUtils.encodePathSegment(certificationDownload.fileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + ".pdf\"";
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(certificationDownload.resource());
    }

}
