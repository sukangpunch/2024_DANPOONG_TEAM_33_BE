package com.onetry.spring.certification.dto.req;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
public record CertificationCreateDto(
        String certificationName,
        String issuingOrganization,
        LocalDate acquisitionDate,
        MultipartFile certificationFile
) {
}
