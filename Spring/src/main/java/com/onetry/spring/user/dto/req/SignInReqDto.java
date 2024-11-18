package com.onetry.spring.user.dto.req;

public record SignInReqDto(
        String email,
        String password
) {
}
