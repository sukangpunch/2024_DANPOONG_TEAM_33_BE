package com.example.onetry.common;

public record CommonResponseDto<T>(
        String message,
        T result
){
}
