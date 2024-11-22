package com.example.onetry.company.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company_extra_info")
@Getter
@NoArgsConstructor
public class CompanyExtraInfo {

    @Id
    private String id;

    private int infoNo; // 공고 번호

    private int applyCount; // 지원자 수

    private int applyAverageScore; // 지원 평균 점수

    private int viewCount; // 조회 수
}
