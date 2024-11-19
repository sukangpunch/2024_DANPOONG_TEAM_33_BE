package com.onetry.spring.userpreference.dto.res;

import com.onetry.spring.userpreference.entity.UserPreference;
import lombok.Builder;

import java.util.List;

@Builder
public record UserPreferenceInfo(
        Long id,

        // 지역 정보
        String region,
        String subRegion,

        // 경력 정보
        String career,

        // 업종 정보
        String industryCategory, // 예: "기획 - 전략"
        String subIndustry, // 예: "마케팅"

        // 학력 정보
        String educationLevel, // 예: "2~3년제", "4년제", "사이버", "고졸"
        List<String> workDays,
        List<String> targetCompanies
) {
    public static UserPreferenceInfo from(UserPreference userPreference){
        return UserPreferenceInfo.builder()
                .id(userPreference.getId())
                .region(userPreference.getRegion())
                .subRegion(userPreference.getSubRegion())
                .career(userPreference.getCareer())
                .industryCategory(userPreference.getIndustryCategory())
                .subIndustry(userPreference.getSubIndustry())
                .educationLevel(userPreference.getEducationLevel())
                .workDays(userPreference.getWorkDays())
                .targetCompanies(userPreference.getTargetCompanies())
                .build();
    }
}
