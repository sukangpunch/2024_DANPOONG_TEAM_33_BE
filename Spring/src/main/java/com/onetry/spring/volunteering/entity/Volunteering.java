package com.onetry.spring.volunteering.entity;

import com.onetry.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "volunteering")
public class Volunteering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String time;
    private String volunteeringFileName;
    private String generateFileName;
    private String volunteeringPath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Volunteering(String time, String volunteeringFileName,String generateFileName, String volunteeringPath, User user){
        this.time = time;
        this.volunteeringFileName = volunteeringFileName;
        this.volunteeringPath = volunteeringPath;
        this.generateFileName = generateFileName;
        this.user = user;
    }
}
