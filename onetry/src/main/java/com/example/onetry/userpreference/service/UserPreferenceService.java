package com.example.onetry.userpreference.service;

import com.example.onetry.exception.CustomException;
import com.example.onetry.exception.ExceptionCode;
import com.example.onetry.mypage.entity.MyPage;
import com.example.onetry.mypage.repository.MyPageRepository;
import com.example.onetry.user.entity.User;
import com.example.onetry.user.repository.UserRepository;
import com.example.onetry.userpreference.dto.req.AddOnboardingDataDto;
import com.example.onetry.userpreference.dto.req.UserPreferenceUpdateDto;
import com.example.onetry.userpreference.dto.res.UserPreferenceInfo;
import com.example.onetry.userpreference.entity.UserPreference;
import com.example.onetry.userpreference.repository.UserPreferenceRepository;
import com.example.onetry.util.NowTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class UserPreferenceService {
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;
    private final MyPageRepository myPageRepository;

    @Transactional(readOnly = true)
    public UserPreferenceInfo getUserPreference(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_EXIST));
        UserPreference userPreference = userPreferenceRepository.findByUser(user).orElseThrow(()->new CustomException(ExceptionCode.USER_NOT_EXIST));
        log.info("[getUserPreference] userPreference: {}",userPreference.toString());
        return UserPreferenceInfo.from(userPreference);
    }

    public UserPreferenceInfo addOnboardingData(String email, AddOnboardingDataDto addOnboardingDataDto) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_EXIST));
        UserPreference userPreference = UserPreference.builder()
                .region(addOnboardingDataDto.region())
                .subRegion(addOnboardingDataDto.subRegion())
                .industryCategory(addOnboardingDataDto.industryCategory())
                .subIndustry(addOnboardingDataDto.subIndustry())
                .career(addOnboardingDataDto.career())
                .targetCompanies(addOnboardingDataDto.targetCompanies())
                .user(user)
                .build();

        userPreferenceRepository.save(userPreference);
        myPageUpdate(user);

        return UserPreferenceInfo.from(userPreference);
    }

    public UserPreferenceInfo updateUserPreference(String email, UserPreferenceUpdateDto userPreferenceUpdateDto) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_EXIST));
        UserPreference userPreference = userPreferenceRepository.findByUser(user).orElseThrow(()->new CustomException(ExceptionCode.USER_PREFERENCE_NOT_EXIST));

        if (userPreferenceUpdateDto.region() != null || userPreferenceUpdateDto.subRegion() != null) {
            userPreference.updateRegion(userPreferenceUpdateDto.region(), userPreferenceUpdateDto.subRegion());
        }

        if (userPreferenceUpdateDto.career() != null) {
            userPreference.updateCareer(userPreferenceUpdateDto.career());
        }

        if (userPreferenceUpdateDto.industryCategory() != null || userPreferenceUpdateDto.subIndustry() != null) {
            userPreference.updateIndustry(userPreferenceUpdateDto.industryCategory(), userPreferenceUpdateDto.subIndustry());
        }

        if (userPreferenceUpdateDto.educationLevel() != null) {
            userPreference.updateEducationLevel(userPreferenceUpdateDto.educationLevel());
        }

        if (userPreferenceUpdateDto.workDays() != null) {
            userPreference.updateWorkDays(userPreferenceUpdateDto.workDays());
        }

        if (userPreferenceUpdateDto.targetCompanies() != null) {
            userPreference.updateTargetCompanies(userPreferenceUpdateDto.targetCompanies());
        }
        myPageUpdate(user);
        return UserPreferenceInfo.from(userPreference);
    }

    public void myPageUpdate(User user){
        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(() -> new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.updatePreferenceModified(NowTimeUtil.timeNowInZone("Asia/Seoul"));
    }
}
