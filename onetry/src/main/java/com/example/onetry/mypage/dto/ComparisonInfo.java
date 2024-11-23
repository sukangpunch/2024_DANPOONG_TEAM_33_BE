package com.example.onetry.mypage.dto;

import lombok.Builder;

@Builder
public record ComparisonInfo(
        String certificationCompare,
        String volunteeringCompare,
        String portfolioCompare,
        int averCertificationCount,
        int averVolunteeringCount,
        int averPortfolioCount,
        int myCertificationCount,
        int myVolunteeringCount,
        int myPortfolioCount
) {
    public static ComparisonInfo of(String certificationCompare, String volunteeringCompare, String portfolioCompare,
                                    int averCertificationCount, int averVolunteeringCount, int averPortfolioCount,
                                    int myCertificationCount, int myVolunteeringCount, int myPortfolioCount){

        return ComparisonInfo.builder()
                .certificationCompare(certificationCompare)
                .volunteeringCompare(volunteeringCompare)
                .portfolioCompare(portfolioCompare)
                .averCertificationCount(averCertificationCount)
                .averVolunteeringCount(averVolunteeringCount)
                .averPortfolioCount(averPortfolioCount)
                .myCertificationCount(myCertificationCount)
                .myVolunteeringCount(myVolunteeringCount)
                .myPortfolioCount(myPortfolioCount)
                .build();
    }
}
