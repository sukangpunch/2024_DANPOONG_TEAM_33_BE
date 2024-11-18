package com.onetry.spring.oauth.dto;


import com.onetry.spring.user.entity.User;
import lombok.Builder;

@Builder
public record CheckSignUserDto(
        boolean isNewUser,
        User user
) {
    public static CheckSignUserDto of(boolean isNewUser, User user){
        return CheckSignUserDto.builder()
                .isNewUser(isNewUser)
                .user(user)
                .build();
    }
}
