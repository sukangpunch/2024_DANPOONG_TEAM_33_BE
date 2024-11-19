package com.onetry.spring.userpreference.service;


import com.onetry.spring.exception.CustomException;
import com.onetry.spring.exception.ExceptionCode;
import com.onetry.spring.user.entity.User;
import com.onetry.spring.user.repository.UserRepository;
import com.onetry.spring.userpreference.dto.req.AddOnboardingDataDto;
import com.onetry.spring.userpreference.dto.req.UserPreferenceUpdateDto;
import com.onetry.spring.userpreference.dto.res.UserPreferenceInfo;
import com.onetry.spring.userpreference.entity.UserPreference;
import com.onetry.spring.userpreference.repository.UserPreferenceRepository;
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

        return UserPreferenceInfo.from(userPreference);
    }
}
