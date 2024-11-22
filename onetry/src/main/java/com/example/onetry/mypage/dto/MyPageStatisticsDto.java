package com.example.onetry.mypage.dto;


import lombok.Getter;

@Getter
public class MyPageStatisticsDto {
    private Double  portfolioCount;
    private Double  volunteeringTime;
    private Double  certificationCount;

    public MyPageStatisticsDto(Double portfolioCount, Double volunteeringTime, Double certificationCount) {
        this.portfolioCount = (portfolioCount != null) ? portfolioCount : 0;
        this.volunteeringTime = (volunteeringTime != null) ? volunteeringTime : 0;
        this.certificationCount = (certificationCount != null) ? certificationCount : 0;
    }

}
