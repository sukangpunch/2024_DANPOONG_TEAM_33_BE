package com.example.onetry.oauth.dto.res;

import lombok.Builder;

@Builder
public record KaKaoTokenResDto(
        String access_token,
        String refresh_token

) {
}
