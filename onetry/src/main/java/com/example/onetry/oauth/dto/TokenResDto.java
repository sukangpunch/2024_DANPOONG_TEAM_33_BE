package com.example.onetry.oauth.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record TokenResDto(
        Map<String, String> tokens,
        boolean isNewUser
) {
    public static TokenResDto of(Map<String,String> tokens, boolean isNewUser)
    {
        return TokenResDto.builder()
                .tokens(tokens)
                .isNewUser(isNewUser)
                .build();
    }
}
