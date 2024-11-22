package com.example.onetry.portfolio.service;

import com.example.onetry.common.FileDownload;
import com.example.onetry.common.WriteFileToFileSystemResult;
import com.example.onetry.component.file.FileComponent;
import com.example.onetry.exception.CustomException;
import com.example.onetry.exception.ExceptionCode;
import com.example.onetry.mypage.entity.MyPage;
import com.example.onetry.mypage.repository.MyPageRepository;
import com.example.onetry.portfolio.dto.req.PortfolioCreateDto;
import com.example.onetry.portfolio.dto.res.PortfolioInfo;
import com.example.onetry.portfolio.entity.Portfolio;
import com.example.onetry.portfolio.repository.PortfolioRepository;
import com.example.onetry.user.entity.User;
import com.example.onetry.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final FileComponent fileComponent;
    private final MyPageRepository myPageRepository;

    @Transactional(readOnly = true)
    public PortfolioInfo getPortfolioInfo(Long portfolioId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ExceptionCode.USER_NOT_EXIST));
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(()->new CustomException(ExceptionCode.PORTFOLIO_NOT_EXIST));

        if(!user.getId().equals(portfolio.getUser().getId())){
            throw new CustomException(ExceptionCode.PORTFOLIO_NOT_EXIST);
        }

        return PortfolioInfo.of(portfolio, portfolio.getGenerateFileName());
    }

    @Transactional(readOnly = true)
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

        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(()->new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.plusPortfolioCount();

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

        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(() -> new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.minusPortfolioCount();
    }

    public FileDownload downloadPortfolioFromFileSystem(String generateFileName) {
        Portfolio portfolio= portfolioRepository.findByGenerateFileName(generateFileName).orElseThrow(()-> new CustomException(ExceptionCode.CERTIFICATION_NOT_EXIST));
        Resource resource =  fileComponent.downloadFromResourceFileSystem(portfolio.getPortfolioPath());
        return FileDownload.of(resource, portfolio.getPortfolioName());
    }
}
