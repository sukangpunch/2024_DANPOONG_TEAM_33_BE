package com.example.onetry.resume.dto.res;

import com.example.onetry.resume.entity.Experience;
import com.example.onetry.resume.entity.Resume;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ResumeInfo(
        Long id,
        String idPhotoUrl,
        String name,
        LocalDate birthDate,
        String nationalId,
        String address,
        String email,
        List<EducationDto> educationList,
        List<ExperienceDto> experienceList
) {
    public static ResumeInfo from(Resume resume){
        return ResumeInfo.builder()
                .id(resume.getId())
                .idPhotoUrl(resume.getIdPhotoUrl())
                .name(resume.getName())
                .birthDate(resume.getBirthDate())
                .address(resume.getAddress())
                .email(resume.getEmail())
                .build();
    }

    public static ResumeInfo of(Resume resume, List<ExperienceDto> experienceList, List<EducationDto> educationList){
        return ResumeInfo.builder()
                .id(resume.getId())
                .idPhotoUrl(resume.getIdPhotoUrl())
                .name(resume.getName())
                .birthDate(resume.getBirthDate())
                .address(resume.getAddress())
                .email(resume.getEmail())
                .educationList(educationList)
                .experienceList(experienceList)
                .build();
    }
}
