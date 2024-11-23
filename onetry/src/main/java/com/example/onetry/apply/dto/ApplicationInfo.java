package com.example.onetry.apply.dto;

import com.example.onetry.apply.entity.Application;
import com.example.onetry.apply.entity.ApplicationState;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
public record ApplicationInfo(
        Long id,
        Long applyId,
        String title,
        String companyName,
        LocalDate applicationDeadLine,
        int applicantCount,
        List<String> requiredCertifications,
        String userGrade,
        String averageGrade,
        ApplicationState status
) {
    public static ApplicationInfo from(Application application){
        return ApplicationInfo.builder()
                .id(application.getId())
                .applyId(application.getApplyId())
                .title(application.getTitle())
                .companyName(application.getCompanyName())
                .applicationDeadLine(application.getApplicationDeadLine())
                .applicantCount(application.getApplicantCount())
                .requiredCertifications(new ArrayList<>(application.getRequiredCertifications()))
                .userGrade(application.getUserGrade())
                .averageGrade(application.getAverageGrade())
                .status(application.getStatus())
                .build();
    }
}
