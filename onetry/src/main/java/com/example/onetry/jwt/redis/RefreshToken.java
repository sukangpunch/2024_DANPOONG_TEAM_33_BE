package com.example.onetry.jwt.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 604800)
public class RefreshToken {

    private String refreshToken;

    @Id
    @Indexed
    private Long id;
    public RefreshToken(Long id, String refreshToken){
        this.refreshToken = refreshToken;
        this.id = id;
    }

    // 기본 생성자 (필수)
    public RefreshToken() {
    }
}
