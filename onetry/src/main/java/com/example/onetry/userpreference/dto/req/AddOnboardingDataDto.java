package com.example.onetry.userpreference.dto.req;

import java.util.List;

public record AddOnboardingDataDto(
        String industryCategory,
        String subIndustry,
        String region,
        String subRegion,
        String career,
        List<String> targetCompanies
) {}
