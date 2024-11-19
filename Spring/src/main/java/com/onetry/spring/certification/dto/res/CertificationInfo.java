package com.onetry.spring.certification.dto.res;

import com.onetry.spring.certification.entity.Certification;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CertificationInfo(
        Long certificationId,
        String certificationName,
        String issuingOrganization,
        LocalDate acquisitionDate,
        String certificationUrl

){
    public static CertificationInfo of(Certification certification, String certificationUrl){
        return CertificationInfo.builder()
                .certificationId(certification.getId())
                .certificationName(certification.getCertificationName())
                .issuingOrganization(certification.getIssuingOrganization())
                .acquisitionDate(certification.getAcquisitionDate())
                .certificationUrl(certificationUrl)
                .build();
    }
}
