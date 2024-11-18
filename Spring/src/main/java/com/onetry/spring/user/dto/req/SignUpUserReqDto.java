package com.onetry.spring.user.dto.req;

public record SignUpUserReqDto(
        String name,
        String email,
        String password
) {}
