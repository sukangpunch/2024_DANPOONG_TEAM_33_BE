package com.example.onetry.certification.service;

import com.example.onetry.certification.dto.req.CertificationCreateDto;
import com.example.onetry.certification.dto.res.CertificationInfo;
import com.example.onetry.certification.entity.Certification;
import com.example.onetry.certification.repository.CertificationRepository;
import com.example.onetry.common.FileDownload;
import com.example.onetry.common.WriteFileToFileSystemResult;
import com.example.onetry.component.file.FileComponent;
import com.example.onetry.exception.CustomException;
import com.example.onetry.exception.ExceptionCode;
import com.example.onetry.mypage.entity.MyPage;
import com.example.onetry.mypage.repository.MyPageRepository;
import com.example.onetry.user.entity.User;
import com.example.onetry.user.repository.UserRepository;
import com.example.onetry.util.TimeAgoCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final FileComponent fileComponent;
    private final MyPageRepository myPageRepository;

    @Transactional(readOnly = true)
    public CertificationInfo getCertification(Long certificationId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ExceptionCode.USER_NOT_EXIST));
        Certification certification = certificationRepository.findById(certificationId).orElseThrow(()-> new CustomException(ExceptionCode.CERTIFICATION_NOT_EXIST));
        if(!certification.getUser().getId().equals(user.getId())){
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS_CERTIFICATION);
        }

        return CertificationInfo.of(certification,certification.getGenerateFileName());
    }

    @Transactional(readOnly = true)
    public List<CertificationInfo> getAllCertification(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException(ExceptionCode.USER_NOT_EXIST));
        List<Certification> certifications = certificationRepository.findByUser(user);

        // certification 객체 리스트를 스트림으로 변환 -> 각각의 certification 객체를 CertificationDetail 객체의 from 를 적용,
        // 스트림의 요소들을 수집하여 최종 리스트로 만듬
        return certifications.stream().map(certification -> {
            return CertificationInfo.of(certification, certification.getGenerateFileName());
        }).collect(Collectors.toList());
    }

    public CertificationInfo addCertification(String email, CertificationCreateDto certificationCreateDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
        WriteFileToFileSystemResult writeFileToFileSystemResult = fileComponent.uploadFromFileSystem(certificationCreateDto.certificationFile().getOriginalFilename(),certificationCreateDto.certificationFile());

        Certification certification = Certification.builder()
                .certificationName(certificationCreateDto.certificationName())
                .acquisitionDate(certificationCreateDto.acquisitionDate())
                .issuingOrganization(certificationCreateDto.issuingOrganization())
                .certificationPath(writeFileToFileSystemResult.filePath())
                .generateFileName(writeFileToFileSystemResult.generateName())
                .user(user)
                .build();

        certificationRepository.save(certification);
        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(()-> new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.plusCertificationCount();
        myPage.updateCertificationModified(timeNowInZone("Asia/Seoul"));

        return CertificationInfo.of(certification, writeFileToFileSystemResult.generateName());
    }

    @Transactional(readOnly = true)
    public FileDownload downloadCertificationFromFileSystem(String generateFileName){
        Certification certification = certificationRepository.findByGenerateFileName(generateFileName).orElseThrow(()-> new CustomException(ExceptionCode.CERTIFICATION_NOT_EXIST));
        Resource resource =  fileComponent.downloadFromResourceFileSystem(certification.getCertificationPath());
        return FileDownload.of(resource, certification.getCertificationName());
    }

    public void deleteCertification(Long certificationId, String email) {
        Certification certification = certificationRepository.findById(certificationId).orElseThrow(() -> new CustomException(ExceptionCode.CERTIFICATION_NOT_EXIST));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));

        if(!certification.getUser().getId().equals(user.getId())){
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS_CERTIFICATION);
        }
        String fileName = certification.getGenerateFileName();
        fileComponent.deleteFileFromFileSystem(fileName);
        certificationRepository.delete(certification);

        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(()-> new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.minusCertificationCount();
        myPage.updateCertificationModified(timeNowInZone("Asia/Seoul"));
    }

    public LocalDateTime timeNowInZone(String zoneId){
        return ZonedDateTime.now(ZoneId.of(zoneId)).toLocalDateTime();
    }
}


