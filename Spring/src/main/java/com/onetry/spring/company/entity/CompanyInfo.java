package com.onetry.spring.company.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "company_info")
@Getter
@NoArgsConstructor
public class CompanyInfo {

    @Id
    private String id; // MongoDB _id

    private Long infoNo; // 공고 번호

    private String position; // 직책

    private String companyName; // 회사명

    private String companyLocationRegion; // 회사 위치 (지역)

    private String companyLocationSubregion; // 회사 위치 (하위 지역)

    private String companyImageURL; // 회사 이미지 URL

    private String companyRating; // 회사 평점

    private String requiredEducation; // 필수 학력

    private int salaryMin; // 최소 연봉

    private int salaryMax; // 최대 연봉

    private String salaryCurrency; // 연봉 통화 단위 (예: KRW)

    private String salaryUnit; // 연봉 단위 (예: 만)

    private String employmentType; // 고용 형태

    private String workingScheduleWorkingHours; // 근무 시간

    private List<String> workingScheduleWorkingDays; // 근무 요일

    private LocalDateTime hiringPeriodStartDate; // 채용 시작 날짜

    private LocalDateTime hiringPeriodEndDate; // 채용 종료 날짜

    private int hiringPeriodRecruitmentNumber; // 모집 인원

    private List<CompanyCertification> certificationsEssential; // 필수 자격증

    private List<CompanyCertification> certificationsAdditional; // 추가 자격증

    private int portfolioRequired; // 포트폴리오 필수 여부

    private int portfolioScore; // 포트폴리오 점수

    private String experienceLevel; // 경력 수준 (예: 경력, 신입)

    private int experienceRequiredYears; // 경력 요구 연수

}
