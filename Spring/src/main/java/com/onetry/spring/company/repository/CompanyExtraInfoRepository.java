package com.onetry.spring.company.repository;

import com.onetry.spring.company.entity.CompanyExtraInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyExtraInfoRepository extends MongoRepository<CompanyExtraInfo, String> {
    CompanyExtraInfo findByInfoNo(Long infoNo);
}
