package com.example.onetry.user.dto.res;

import lombok.Builder;

@Builder
public record SignInResDto(
        String accessToken,
        String refreshToken
) {
    public static SignInResDto of(String accessToken, String refreshToken){
        return SignInResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
