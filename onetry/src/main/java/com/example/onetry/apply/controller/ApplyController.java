package com.example.onetry.apply.controller;

import com.example.onetry.apply.dto.ApplyCreateDto;
import com.example.onetry.apply.dto.ApplyInfo;
import com.example.onetry.apply.service.ApplyService;
import com.example.onetry.common.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="[Apply] 지원서를 임시 저장 혹은 제출 합니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/apply")

public class ApplyController {
    private final ApplyService applyService;

    @Operation(summary = "지원서 지원/저장", description = "'TEMPORARY' : 임시저장, 'SUBMITTED' : 지원")
    @PostMapping("/temporary")
    public ResponseEntity<CommonResponseDto<ApplyInfo>> saveApply(
            @RequestBody ApplyCreateDto applyCreateDto,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        ApplyInfo applyInfo = applyService.saveApply(email, applyCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "성공적으로 지원서를 임시 저장 하였습니다.",
                applyInfo));
    }
}
