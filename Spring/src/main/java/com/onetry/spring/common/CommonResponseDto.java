package com.onetry.spring.common;

public record CommonResponseDto<T>(
        String message,
        T result
){
}
