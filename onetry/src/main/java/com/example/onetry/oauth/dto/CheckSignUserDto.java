package com.example.onetry.oauth.dto;


import com.example.onetry.user.entity.User;
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
