package com.example.onetry.resume.controller;

import com.example.onetry.common.CommonResponseDto;
import com.example.onetry.resume.dto.req.ResumeUpdateDto;
import com.example.onetry.resume.dto.res.ResumeInfo;
import com.example.onetry.resume.service.ResumeService;
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
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "[Resume] 이력서를 조회, 수정합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/resume")
@Log4j2
public class ResumeController {

    private final ResumeService resumeService;

    @Operation(summary = "이력서 조회",description = "유저의 이력서를 조회합니다.")
    @GetMapping("/info")
    public ResponseEntity<CommonResponseDto<ResumeInfo>> getResume(
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        ResumeInfo resumeInfo = resumeService.getResume(email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "이력서 조회를 성공적으로 완료하였습니다.",
                resumeInfo
        ));
    }

    @Operation(summary = "이력서 수정",description = "유저의 이력서를 수정합니다.")
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponseDto<ResumeInfo>> updateResume(
            @RequestPart("postUpdateReqDto") ResumeUpdateDto resumeUpdateDto,
            @RequestPart(value = "idPhoto",required = false) MultipartFile idPhoto,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();;
        ResumeInfo resumeInfo = resumeService.updateResume(email, resumeUpdateDto, idPhoto);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "이력서를 성공적으로 수정하였습니다.",
                resumeInfo
        ));
    }

    @Operation(summary = "이력서 증명사진 다운로드", description = "이력서 증명사진을 다운로드합니다.")
    @GetMapping("/id-photo/{id}")
    public ResponseEntity<byte[]> downloadIdPhoto(@PathVariable("id") Long photoId){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(resumeService.downloadIdPhotoFromFileSystem(photoId));
    }
}
