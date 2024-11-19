package com.onetry.spring.portfolio.service;


import com.onetry.spring.common.WriteFileToFileSystemResult;
import com.onetry.spring.component.file.FileComponent;
import com.onetry.spring.exception.CustomException;
import com.onetry.spring.exception.ExceptionCode;
import com.onetry.spring.portfolio.dto.req.PortfolioCreateDto;
import com.onetry.spring.portfolio.dto.res.PortfolioInfo;
import com.onetry.spring.portfolio.entity.Portfolio;
import com.onetry.spring.portfolio.repository.PortfolioRepository;
import com.onetry.spring.user.entity.User;
import com.onetry.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final FileComponent fileComponent;

    public PortfolioInfo getPortfolioInfo(Long portfolioId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ExceptionCode.USER_NOT_EXIST));
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(()->new CustomException(ExceptionCode.PORTFOLIO_NOT_EXIST));

        if(!user.getId().equals(portfolio.getUser().getId())){
            throw new CustomException(ExceptionCode.PORTFOLIO_NOT_EXIST);
        }

        return PortfolioInfo.of(portfolio, portfolio.getGenerateFileName());
    }

    public List<PortfolioInfo> getAllPortfolio(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ExceptionCode.USER_NOT_EXIST));
        List<Portfolio> portfolios = portfolioRepository.findByUser(user);

        // certification 객체 리스트를 스트림으로 변환 -> 각각의 certification 객체를 CertificationDetail 객체의 from 를 적용,
        // 스트림의 요소들을 수집하여 최종 리스트로 만듬
        return portfolios.stream().map(portfolio -> {
            return PortfolioInfo.of(portfolio, portfolio.getGenerateFileName());
        }).collect(Collectors.toList());
    }

    public PortfolioInfo addCreatePortfolio(String email, PortfolioCreateDto portfolioCreateDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
        WriteFileToFileSystemResult writeFileToFileSystemResult = fileComponent.uploadFromFileSystem(portfolioCreateDto.certificationFile().getOriginalFilename(),portfolioCreateDto.certificationFile());

        Portfolio portfolio = Portfolio.builder()
                .portfolioName(portfolioCreateDto.portfolioName())
                .portfolioPath(writeFileToFileSystemResult.filePath())
                .generateFileName(writeFileToFileSystemResult.generateName())
                .user(user)
                .build();

        portfolioRepository.save(portfolio);

        return PortfolioInfo.of(portfolio, writeFileToFileSystemResult.generateName());
    }

    public void deletePortfolio(Long portfolioId, String email) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> new CustomException(ExceptionCode.PORTFOLIO_NOT_EXIST));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));

        if(!portfolio.getUser().getId().equals(user.getId())){
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS_CERTIFICATION);
        }
        String fileName = portfolio.getGenerateFileName();
        fileComponent.deleteFileFromFileSystem(fileName);
        portfolioRepository.delete(portfolio);
    }
}
