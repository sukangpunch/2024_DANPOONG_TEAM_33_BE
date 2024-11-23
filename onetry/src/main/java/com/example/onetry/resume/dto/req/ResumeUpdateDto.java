package com.example.onetry.resume.dto.req;

import java.time.LocalDate;
import java.util.List;

public record ResumeUpdateDto(
        String name,
        String email,
        LocalDate birthDate,
        String nationalId,
        String address,

        List<EducationUpdateDto> educationUpdateDtoList,
        List<ExperienceUpdateDto> experienceUpdateList
) {
}
