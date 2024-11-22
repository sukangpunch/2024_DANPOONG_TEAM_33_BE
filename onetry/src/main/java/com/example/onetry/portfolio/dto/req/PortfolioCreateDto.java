package com.example.onetry.portfolio.dto.req;

import org.springframework.web.multipart.MultipartFile;

public record PortfolioCreateDto(
        String portfolioName,
        MultipartFile certificationFile
) {
}
