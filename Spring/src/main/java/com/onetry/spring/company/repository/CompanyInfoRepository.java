package com.onetry.spring.company.repository;

import com.onetry.spring.company.entity.CompanyInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyInfoRepository extends MongoRepository<CompanyInfo, String> {
    // infoNo를 기준으로 특정 회사 정보를 찾는 메서드
    CompanyInfo findByInfoNo(Long infoNo);
}
