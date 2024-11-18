package com.onetry.spring.user.service;

import com.onetry.spring.component.file.FileComponent;
import com.onetry.spring.exception.CustomException;
import com.onetry.spring.exception.ExceptionCode;
import com.onetry.spring.user.dto.req.UpdateUserInfo;
import com.onetry.spring.user.dto.res.UserInfoResDto;
import com.onetry.spring.user.entity.User;
import com.onetry.spring.user.repository.UserRepository;
import com.onetry.spring.util.FileNameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final FileComponent fileComponent;

    @Value("${fileSystemPath}")
    private String FOLDER_PATH;

    @Transactional(readOnly = true)
    public UserInfoResDto getUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_EXIST));
        return UserInfoResDto.from(user);
    }

    public byte[] downloadProfileFromFileSystem(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_EXIST));
        byte[] profileImg= fileComponent.downloadProfileFromFileSystem(user.getProfileFilePath());
        return profileImg;
    }

    public UserInfoResDto updateProfileImg(String email, MultipartFile updateFile) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_EXIST));
        String updateFileName = FileNameGenerator.generatorName(updateFile.getOriginalFilename());
        String updateFilePath = FOLDER_PATH + updateFileName;
        log.info("새로운 이미지 경로 : {}", updateFilePath);

        fileComponent.updateFileSystemProfileImg(user.getProfileFilePath(), updateFilePath, updateFile);

        user.updateUserProfileUrl(updateFilePath);

        return UserInfoResDto.from(user);
    }

    public UserInfoResDto updateUserInfo(String email, UpdateUserInfo updateUserInfo) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException());
        if(updateUserInfo.name() != null){
            user.updateName(updateUserInfo.name());
        }
        return UserInfoResDto.from(user);
    }

    public void deleteUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException());
        userRepository.deleteById(user.getId());
    }
}
