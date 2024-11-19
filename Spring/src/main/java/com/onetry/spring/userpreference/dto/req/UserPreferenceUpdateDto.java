package com.onetry.spring.userpreference.dto.req;

import java.util.List;

public record UserPreferenceUpdateDto(
        String industryCategory,
        String subIndustry,
        String region,
        String subRegion,
        String career,
        String educationLevel,
        List<String> workDays,
        List<String> targetCompanies
) {
}
