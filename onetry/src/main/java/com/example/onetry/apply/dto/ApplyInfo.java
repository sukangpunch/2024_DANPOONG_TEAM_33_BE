package com.example.onetry.apply.dto;

import com.example.onetry.apply.entity.ApplicationState;
import com.example.onetry.apply.entity.Apply;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ApplyInfo(
        Long id,
        Long companyId,
        String portfolio,
        String volunteeringTime,
        String averageGrade,
        String userGrade,
        int applicantCount,
        LocalDate applicationDeadLine,
        List<String> additionalCertification,
        ApplicationState status

) {
    public static ApplyInfo from(Apply apply){
        return ApplyInfo.builder()
                .id(apply.getId())
                .companyId(apply.getCompanyId())
                .portfolio(apply.getPortfolio())
                .volunteeringTime(apply.getVolunteeringTime())
                .averageGrade(apply.getAverageGrade())
                .userGrade(apply.getUserGrade())
                .applicantCount(apply.getApplicantCount())
                .applicationDeadLine(apply.getApplicationDeadLine())
                .additionalCertification(apply.getAdditionalCertification())
                .status(apply.getStatus())
                .build();
    }
}
