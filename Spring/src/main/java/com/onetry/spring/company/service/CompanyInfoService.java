package com.onetry.spring.company.service;


import com.onetry.spring.company.entity.CompanyExtraInfo;
import com.onetry.spring.company.entity.CompanyInfo;
import com.onetry.spring.company.repository.CompanyExtraInfoRepository;
import com.onetry.spring.company.repository.CompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;
    private final CompanyExtraInfoRepository companyExtraInfoRepository;

    // 전체 CompanyInfo 목록 조회
    public List<CompanyInfo> getAllCompanyInfo() {
        List<CompanyInfo> companyInfoList = companyInfoRepository.findAll();
        return companyInfoList;

    }

    // 특정 CompanyInfo 조회
    public CompanyInfo getCompanyInfoById(Long id) {
        CompanyInfo companyInfo = companyInfoRepository.findByInfoNo(id);
        return companyInfo;
    }

    public List<CompanyExtraInfo> getAllCompanyExtraInfo() {
        List<CompanyExtraInfo> companyExtraInfos = companyExtraInfoRepository.findAll();
        return companyExtraInfos;
    }
    public CompanyExtraInfo getCompanyExtraInfoById(Long id) {
        CompanyExtraInfo companyExtraInfo = companyExtraInfoRepository.findByInfoNo(id);
        return companyExtraInfo;
    }


}
