package com.example.onetry.apply.service;

import com.example.onetry.apply.dto.ApplyCreateDto;
import com.example.onetry.apply.dto.ApplyInfo;
import com.example.onetry.apply.entity.Application;
import com.example.onetry.apply.entity.Apply;
import com.example.onetry.apply.repository.ApplicationRepository;
import com.example.onetry.apply.repository.ApplyRepository;
import com.example.onetry.company.entity.CompanyCertification;
import com.example.onetry.company.entity.CompanyInfo;
import com.example.onetry.company.repository.CompanyInfoRepository;
import com.example.onetry.exception.CustomException;
import com.example.onetry.exception.ExceptionCode;
import com.example.onetry.mypage.entity.MyPage;
import com.example.onetry.mypage.repository.MyPageRepository;
import com.example.onetry.user.entity.User;
import com.example.onetry.user.repository.UserRepository;
import com.example.onetry.util.NowTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final UserRepository userRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final ApplicationRepository applicationRepository;
    private final MyPageRepository myPageRepository;

    public ApplyInfo saveApply(String email, ApplyCreateDto applyCreateDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));

        Apply apply = Apply.builder()
                .companyId(applyCreateDto.companyId())
                .portfolio(applyCreateDto.portfolio())
                .volunteeringTime(applyCreateDto.volunteeringTime())
                .averageGrade(applyCreateDto.averageGrade())
                .userGrade(applyCreateDto.userGrade())
                .applicantCount(applyCreateDto.applicantCount())
                .applicationDeadLine(applyCreateDto.applicationDeadLine())
                .additionalCertification(applyCreateDto.additionalCertification())
                .status(applyCreateDto.status())
                .user(user)
                .build();

        createApplication(apply, user);
        applyRepository.save(apply);
        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(()-> new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.plusAppliedCompanyCount();
        myPage.updateAppliedCompanyModified(NowTimeUtil.timeNowInZone("Asia/Seoul"));

        return ApplyInfo.from(apply);
    }
    private void createApplication(Apply apply, User user){
        Long companyId = apply.getCompanyId();
        CompanyInfo companyInfo = companyInfoRepository.findByInfoNo(companyId);
        String requiredYears = companyInfo.getExperienceRequiredYears() > 0 ? String.valueOf(companyInfo.getExperienceRequiredYears())+"년" : "";
        String title = companyInfo.getCompanyName() + " " + companyInfo.getEmploymentType() + " " +
                companyInfo.getExperienceLevel() + " " + requiredYears + " 채용공고";
        List<String> requiredCertifications = companyInfo.getCertificationsEssential().stream()
                .map(CompanyCertification::getName).toList();

        Application application = Application.builder()
                .applyId(apply.getId())
                .title(title)
                .companyName(companyInfo.getCompanyName())
                .applicationDeadLine(apply.getApplicationDeadLine())
                .applicantCount(apply.getApplicantCount())
                .requiredCertifications(requiredCertifications)
                .userGrade(apply.getUserGrade())
                .averageGrade(apply.getAverageGrade())
                .status(apply.getStatus())
                .user(user)
                .build();
        applicationRepository.save(application);
    }
}
