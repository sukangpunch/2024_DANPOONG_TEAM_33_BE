package com.example.onetry.user.dto.res;

import lombok.Builder;

@Builder
public record SignInResDto(
        String accessToken
) {
    public static SignInResDto of(String accessToken){
        return SignInResDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
