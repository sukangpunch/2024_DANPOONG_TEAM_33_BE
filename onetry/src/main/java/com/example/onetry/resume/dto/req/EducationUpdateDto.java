package com.example.onetry.resume.dto.req;

public record EducationUpdateDto(
        String schoolName,
        String period,
        String major,
        String graduationStatus,
        Double gpa
) {
}
