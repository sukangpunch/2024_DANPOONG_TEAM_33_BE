package com.onetry.spring.transcript.controller;


import com.onetry.spring.common.CommonResponseDto;
import com.onetry.spring.transcript.dto.TranscriptCreateDto;
import com.onetry.spring.transcript.dto.TranscriptInfo;
import com.onetry.spring.transcript.service.TranscriptService;
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

@Tag(name = "[Transcript] Transcript API 성적 증명서를 저장, 조회 , 삭제합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/transcript")
public class TranscriptController {

    private final TranscriptService transcriptService;


    @Operation(summary = "유저의 성정 증명서 전부 조회", description = "유저의 모든 성정증명서를 가져옵니다.")
    @GetMapping("/all")
    public ResponseEntity<CommonResponseDto<List<TranscriptInfo>>> getAllTranscript(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        List<TranscriptInfo> transcriptInfos = transcriptService.getAllTranscript(email);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 모든 성적 증명서를 성공적으로 조회하였습니다.",
                transcriptInfos));
    }

    @Operation(summary = "성적 증명서를 추가합니다.", description = "인증된 유저의 성적 증명서를 추가합니다.")
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto<TranscriptInfo>> addTranscript(@ModelAttribute TranscriptCreateDto transcriptCreateDto,
                                                                                 @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        TranscriptInfo transcriptInfo = transcriptService.addTranscript(email, transcriptCreateDto);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 성적 증명서를 성공적으로 추가 하였습니다.",
                transcriptInfo
        ));
    }

    @Operation(summary = "성적 증명서 삭제", description = "성적 증명서를 삭제합니다.")
    @DeleteMapping("/delete/{transcriptId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteCertification(@PathVariable("transcriptId") Long transcriptId,
                                                                       @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        transcriptService.deleteTranscript(transcriptId, email);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        transcriptId + "번 성적 증명서가 삭제되었습니다",
                        null
                )
        );
    }
}
