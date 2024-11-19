package com.onetry.spring.certification.service;


import com.onetry.spring.certification.dto.req.CertificationCreateDto;
import com.onetry.spring.certification.dto.res.CertificationInfo;
import com.onetry.spring.certification.entity.Certification;
import com.onetry.spring.certification.repository.CertificationRepository;
import com.onetry.spring.common.WriteFileToFileSystemResult;
import com.onetry.spring.component.file.FileComponent;
import com.onetry.spring.exception.CustomException;
import com.onetry.spring.exception.ExceptionCode;
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
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final FileComponent fileComponent;

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

        return CertificationInfo.of(certification, writeFileToFileSystemResult.generateName());
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
    }
}


