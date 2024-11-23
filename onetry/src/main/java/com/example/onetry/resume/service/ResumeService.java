package com.example.onetry.resume.service;

import com.example.onetry.component.file.FileComponent;
import com.example.onetry.exception.CustomException;
import com.example.onetry.exception.ExceptionCode;
import com.example.onetry.mypage.entity.MyPage;
import com.example.onetry.mypage.repository.MyPageRepository;
import com.example.onetry.resume.dto.req.EducationUpdateDto;
import com.example.onetry.resume.dto.req.ExperienceUpdateDto;
import com.example.onetry.resume.dto.req.ResumeUpdateDto;
import com.example.onetry.resume.dto.res.EducationDto;
import com.example.onetry.resume.dto.res.ExperienceDto;
import com.example.onetry.resume.dto.res.ResumeInfo;
import com.example.onetry.resume.entity.Education;
import com.example.onetry.resume.entity.Experience;
import com.example.onetry.resume.entity.Resume;
import com.example.onetry.resume.repository.EducationRepository;
import com.example.onetry.resume.repository.ExperienceRepository;
import com.example.onetry.resume.repository.ResumeRepository;
import com.example.onetry.user.entity.User;
import com.example.onetry.user.repository.UserRepository;
import com.example.onetry.util.FileNameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ResumeService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final FileComponent fileComponent;
    private final MyPageRepository myPageRepository;

    @Value("${fileSystemPath}")
    private String FOLDER_PATH;

    public ResumeInfo getResume(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
        return resumeRepository.getResume(user);
    }

    public ResumeInfo updateResume(String email, ResumeUpdateDto resumeUpdateDto, MultipartFile idPhotoFile) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ExceptionCode.USER_NOT_EXIST));
        Resume resume = resumeRepository.findByUser(user).orElseThrow(()-> new CustomException(ExceptionCode.RESUME_NOT_EXIST));

        resume.updateFromDto(resumeUpdateDto);

        List<Education> educations = educationRepository.findByResumeIdOrderByIdAsc(resume.getId());
        List<EducationUpdateDto> educationUpdateDtos = resumeUpdateDto.educationUpdateDtoList();
        for(int i=0; i<educationUpdateDtos.size();i++){
            educations.get(i).updateFromDto(educationUpdateDtos.get(i));
        }
        List<Experience> experiences = experienceRepository.findByResumeIdOrderByIdAsc(resume.getId());
        List<ExperienceUpdateDto> experienceUpdateDtos = resumeUpdateDto.experienceUpdateList();
        for(int i=0; i<experienceUpdateDtos.size(); i++){
            experiences.get(i).updateFromDto(experienceUpdateDtos.get(i));
        }

        List<EducationDto> educationDtos = educations.stream().map(EducationDto::from).toList();
        List<ExperienceDto> experienceDtos = experiences.stream().map(ExperienceDto::from).toList();

        if(idPhotoFile != null){
            String updateFileName = FileNameGenerator.generatorName(idPhotoFile.getOriginalFilename());
            String updateFilePath = FOLDER_PATH + updateFileName;
            log.info("새로운 이미지 경로 : {}", updateFilePath);

            fileComponent.updateFileSystemProfileImg(resume.getIdPhotoUrl(), updateFilePath, idPhotoFile);
            resume.updateIdPhotoUrl(updateFilePath);
        }

        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(()->new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.updateResumeModified(timeNowInZone("Asia/Seoul"));
        myPage.updateResumeCompletePercentage(calculateResumeCompletion(resume, educations, experiences));

        return ResumeInfo.of(resume, experienceDtos, educationDtos);
    }

    public int calculateResumeCompletion(Resume resume, List<Education> educations, List<Experience> experiences) {
        int completionPercentage = 0;

        // 1. 필수 항목
        if (resume.getIdPhotoUrl() != null && !resume.getIdPhotoUrl().isEmpty()) {
            completionPercentage += 10; // 증명사진
        }
        if (resume.getName() != null && !resume.getName().isEmpty()) {
            completionPercentage += 5; // 이름
        }
        if (resume.getNationalId() != null && !resume.getNationalId().isEmpty()) {
            completionPercentage += 5; // 주민등록번호
        }
        if (resume.getBirthDate() != null) {
            completionPercentage += 5; // 생년월일
        }
        if (resume.getAddress() != null && !resume.getAddress().isEmpty()) {
            completionPercentage += 5; // 주소
        }
        if (resume.getEmail() != null && !resume.getEmail().isEmpty()) {
            completionPercentage += 10; // 이메일
        }

        // 2. 학력 사항
        for(Education education : educations){
            if(isValidEducation(education)){
                completionPercentage += 10;
            }
        }

        // 3. 경력 사항
        for(Experience experience : experiences){
            if(isValidExperience(experience)){
                completionPercentage += 10;
            }
        }

        return Math.min(completionPercentage, 100); // 최대 100%로 제한
    }

    public boolean isValidEducation(Education education) {
        // 조건문을 통해 모든 필드를 확인
        return (education.getPeriod() != null && !education.getPeriod().isEmpty())
                && (education.getSchoolName() != null && !education.getSchoolName().isEmpty())
                && (education.getMajor() != null && !education.getMajor().isEmpty())
                && (education.getGraduationStatus() != null && !education.getGraduationStatus().isEmpty())
                && (education.getGpa() != null && education.getGpa() != 0);
    }

    public boolean isValidExperience(Experience experience) {
        // 조건문을 통해 모든 필드를 확인
        return (experience.getCompanyName() != null && !experience.getCompanyName().isEmpty())
                && (experience.getEmploymentPeriod() != null && !experience.getEmploymentPeriod().isEmpty())
                && (experience.getLastPosition() != null && !experience.getLastPosition().isEmpty())
                && (experience.getResponsibilities() != null && !experience.getResponsibilities().isEmpty());
    }

    public byte[] downloadIdPhotoFromFileSystem(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(()->new CustomException(ExceptionCode.RESUME_NOT_EXIST));
        byte[] idPhotoImg= fileComponent.downloadPhotoFromFileSystem(resume.getIdPhotoUrl());
        return idPhotoImg;
    }

    public LocalDateTime timeNowInZone(String zoneId){
        return ZonedDateTime.now(ZoneId.of(zoneId)).toLocalDateTime();
    }
}
