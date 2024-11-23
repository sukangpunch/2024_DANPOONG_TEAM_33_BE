package com.example.onetry.resume.dto.res;

import com.example.onetry.resume.entity.Experience;
import lombok.Builder;

@Builder
public record ExperienceDto(
        Long id,
        String companyName,
        String employmentPeriod,
        String lastPosition,
        String responsibilities
) {
    public static ExperienceDto from(Experience experience) {
        return ExperienceDto.builder()
                .id(experience.getId())
                .companyName(experience.getCompanyName())
                .employmentPeriod(experience.getEmploymentPeriod())
                .lastPosition(experience.getLastPosition())
                .responsibilities(experience.getResponsibilities())
                .build();
    }

}
