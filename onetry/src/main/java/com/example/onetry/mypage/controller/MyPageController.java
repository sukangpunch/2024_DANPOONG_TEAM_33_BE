package com.example.onetry.mypage.controller;

import com.example.onetry.common.CommonResponseDto;
import com.example.onetry.mypage.dto.ComparisonInfo;
import com.example.onetry.mypage.dto.MyPageInfo;
import com.example.onetry.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[MyPage] 마이페이지, 유저 평균 데이터 조회, 자신의 데이터 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "MyPage 조회", description = "유저의 mypage를 조회합니다.")
    @GetMapping()
    public ResponseEntity<CommonResponseDto<MyPageInfo>> getMyPage(
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        MyPageInfo myPageInfo = myPageService.getMyPageInfo(email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "MyPage 조회를 성공적으로 완료하였습니다.",
                myPageInfo
        ));
    }

    @Operation(summary = "MyPage 계산 결과 조회", description = "유저의 mypage의 자격증, 봉사활동, 포트폴리오 평균 계산 결과를 조회합니다.")
    @GetMapping("/compare")
    public ResponseEntity<CommonResponseDto<ComparisonInfo>> getCompareMyPage(
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        ComparisonInfo comparisonInfo = myPageService.getCompareMyPageInfo(email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "MyPage 계산 결과 조회를 성공적으로 완료하였습니다.",
                comparisonInfo
        ));
    }
}
