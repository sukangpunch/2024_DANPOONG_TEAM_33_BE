package com.onetry.spring.transcript.entity;

import com.onetry.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "transcript")
public class Transcript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transcriptName;
    private String generateFileName;
    private String transcriptPath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    protected Transcript(String transcriptName,String generateFileName, String transcriptPath, User user){
        this.transcriptName= transcriptName;
        this.generateFileName = generateFileName;
        this.transcriptPath = transcriptPath;
        this.user = user;
    }

}
