package com.onetry.spring.oauth.controller;


import com.onetry.spring.common.CommonResponseDto;
import com.onetry.spring.oauth.dto.TokenResDto;
import com.onetry.spring.oauth.service.KaKaoOAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "[KaKao OAuth2] KaKao OAuth2 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class KaKaoOAuth2Controller {
    private final KaKaoOAuth2Service kaKaoOAuth2Service;

    @Operation(summary = "인가 코드 발급", description = "로그인 페이지로 이동 후, 로그인 후 인가 코드 발급")
    @GetMapping(value = "/login-page/kakao")
    public ResponseEntity<Void> getKaKaoAuthUrl(HttpServletRequest request) throws Exception {
        //HttpStatus.MOVED_PERMANENTLY 해야 리턴 된 uri 경로로 바로 이동 할 수 있다.
        HttpHeaders headers = kaKaoOAuth2Service.makeLoginURI(); // 카카오 인증 URL 생성
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(summary = "accessToken 발급", description = "인가 코드를 받아, 회원가입 후 jwt 형식의 accessToken 발급")
    @GetMapping(value = "/login/kakao")
    public ResponseEntity<CommonResponseDto> sign(@RequestParam("code") String authCode) throws Exception{
        TokenResDto tokenResDto = kaKaoOAuth2Service.socialLogin(authCode);
        Map<String,String> tokens = tokenResDto.tokens();
        boolean isNewUser = tokenResDto.isNewUser();

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto(
                isNewUser ? "회원가입" : "로그인",
                tokens));
    }
}
