package com.example.onetry.resume.dto.res;

import com.example.onetry.resume.entity.Education;
import lombok.Builder;

@Builder
public record EducationDto(
        Long id,
        String period,
        String schoolName,
        String major,
        String graduationStatus,
        Double gpa
) {
    public static EducationDto from(Education education){
        return EducationDto.builder()
                .id(education.getId())
                .period(education.getPeriod())
                .schoolName(education.getSchoolName())
                .major(education.getMajor())
                .graduationStatus(education.getGraduationStatus())
                .gpa(education.getGpa())
                .build();
    }
}
