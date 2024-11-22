package com.example.onetry.resume.dto.req;

public record ExperienceUpdateDto(
        String companyName,
        String employmentPeriod,
        String lastPosition,
        String responsibilities
) {
}
