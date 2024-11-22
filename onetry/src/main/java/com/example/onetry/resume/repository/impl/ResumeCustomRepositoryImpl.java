package com.example.onetry.resume.repository.impl;

import com.example.onetry.resume.dto.res.EducationDto;
import com.example.onetry.resume.dto.res.ExperienceDto;
import com.example.onetry.resume.dto.res.ResumeInfo;
import com.example.onetry.resume.entity.Education;
import com.example.onetry.resume.entity.Experience;
import com.example.onetry.resume.entity.Resume;
import com.example.onetry.resume.repository.ResumeCustomRepository;
import com.example.onetry.user.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.onetry.resume.entity.QEducation.education;
import static com.example.onetry.resume.entity.QExperience.experience;
import static com.example.onetry.resume.entity.QResume.resume;
import static com.example.onetry.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ResumeCustomRepositoryImpl implements ResumeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public ResumeInfo getResume(User reqUser){
        List<Tuple> resumeData = jpaQueryFactory
                .select(resume, education, experience).distinct()
                .from(user)
                .join(resume).on(user.id.eq(resume.user.id))
                .leftJoin(education).on(resume.id.eq(education.resume.id))
                .leftJoin(experience).on(resume.id.eq(experience.resume.id))
                .where(user.id.eq(reqUser.getId()))
                .fetch();

        Resume userResume = resumeData.get(0).get(resume);
        Set<EducationDto> educationSet = new HashSet<>();
        Set<ExperienceDto> experienceSet = new HashSet<>();

        for (Tuple tuple : resumeData) {
            Education edu = tuple.get(education);
            if (edu != null) {
                educationSet.add(EducationDto.from(edu));
            }
            Experience exp = tuple.get(experience);
            if (exp != null) {
                experienceSet.add(ExperienceDto.from(exp));
            }
        }

        List experienceList = experienceSet.stream()
                .sorted(Comparator.comparing(ExperienceDto::id))
                .toList();
        List educationList = educationSet.stream()
                .sorted(Comparator.comparing(EducationDto::id))
                .toList();

        return ResumeInfo.builder()
                .id(userResume.getId())
                .idPhotoUrl(userResume.getIdPhotoUrl())
                .name(userResume.getName())
                .nationalId(userResume.getNationalId())
                .email(userResume.getEmail())
                .birthDate(userResume.getBirthDate())
                .address(userResume.getAddress())
                .educationList(educationList.stream().toList())
                .experienceList(experienceList.stream().toList())
                .build();
    }
}
