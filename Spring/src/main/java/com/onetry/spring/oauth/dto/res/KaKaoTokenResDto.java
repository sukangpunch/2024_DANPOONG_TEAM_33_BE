package com.onetry.spring.oauth.dto.res;

import lombok.Builder;

@Builder
public record KaKaoTokenResDto(
        String access_token,
        String refresh_token

) {
}
