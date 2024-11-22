package com.example.onetry.mypage.dto;

import com.example.onetry.mypage.entity.MyPage;
import lombok.Builder;

@Builder
public record MyPageInfo(
        int resumeCompletionPercentage,
        String resumeTimeAgo,
        int certificationCount,
        String certificationTimeAgo,
        int appliedCompanyCount,
        String appliedTimeAgo,
        String preferenceTimeAgo
) {
    public static MyPageInfo of(MyPage myPage,
                                String resumeTimeAgo,
                                String certificationTimeAgo,
                                String appliedTimeAgo,
                                String preferenceTimeAgo){
        return MyPageInfo.builder()
                .resumeCompletionPercentage(myPage.getResumeCompletionPercentage())
                .resumeTimeAgo(resumeTimeAgo)
                .certificationCount(myPage.getCertificationCount())
                .certificationTimeAgo(certificationTimeAgo)
                .appliedCompanyCount(myPage.getAppliedCompanyCount())
                .appliedTimeAgo(appliedTimeAgo)
                .preferenceTimeAgo(preferenceTimeAgo)
                .build();
    }
}
