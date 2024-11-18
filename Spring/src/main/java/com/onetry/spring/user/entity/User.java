package com.onetry.spring.user.entity;


import com.onetry.spring.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(name = "profile_file_path")
    private String profileFilePath;

    @Builder
    protected User(String email, String name,String role,String profileFilePath){
        this.name = name;
        this.email = email;
        this.role = role;
        this.username = "kakao";
        this.profileFilePath = profileFilePath;
    }

    public void updateName(String name){
        this.name = name;
    }
    public void updateUserProfileUrl(String profileFilePath){
        this.profileFilePath = profileFilePath;
    }
}

