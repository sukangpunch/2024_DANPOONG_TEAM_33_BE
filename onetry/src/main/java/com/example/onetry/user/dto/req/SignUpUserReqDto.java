package com.example.onetry.user.dto.req;

public record SignUpUserReqDto(
        String name,
        String email,
        String password
) {}
