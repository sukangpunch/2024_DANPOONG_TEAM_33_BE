package com.example.onetry.oauth.dto.res;

import lombok.Builder;

@Builder
public record KaKaoUserInfoResDto(
        String email,
        String nickname
) {
}
