package com.example.onetry.apply.dto;

import com.example.onetry.apply.entity.ApplicationState;

import java.time.LocalDate;
import java.util.List;

public record ApplyCreateDto(
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
}
