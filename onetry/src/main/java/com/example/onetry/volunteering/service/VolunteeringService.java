package com.example.onetry.volunteering.service;

import com.example.onetry.common.WriteFileToFileSystemResult;
import com.example.onetry.component.file.FileComponent;
import com.example.onetry.exception.CustomException;
import com.example.onetry.exception.ExceptionCode;
import com.example.onetry.mypage.entity.MyPage;
import com.example.onetry.mypage.repository.MyPageRepository;
import com.example.onetry.user.entity.User;
import com.example.onetry.user.repository.UserRepository;
import com.example.onetry.volunteering.dto.VolunteeringCreateDto;
import com.example.onetry.volunteering.dto.VolunteeringInfo;
import com.example.onetry.volunteering.entity.Volunteering;
import com.example.onetry.volunteering.repository.VolunteeringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VolunteeringService {

    private final VolunteeringRepository volunteeringRepository;
    private final UserRepository userRepository;
    private final FileComponent fileComponent;
    private final MyPageRepository myPageRepository;

    public List<VolunteeringInfo> getAllVolunteering(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
        List<Volunteering> volunteerings = volunteeringRepository.findByUser(user);

        return volunteerings.stream().map(VolunteeringInfo::from).toList();
    }

    public VolunteeringInfo addVolunteering(String email, VolunteeringCreateDto volunteeringCreateDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
        WriteFileToFileSystemResult writeFileToFileSystemResult = fileComponent.uploadFromFileSystem(volunteeringCreateDto.volunteeringFile().getOriginalFilename(), volunteeringCreateDto.volunteeringFile());

        Volunteering volunteering = Volunteering.builder()
                .time(volunteeringCreateDto.time())
                .volunteeringFileName(volunteeringCreateDto.volunteeringFileName())
                .generateFileName(writeFileToFileSystemResult.generateName())
                .volunteeringPath(writeFileToFileSystemResult.filePath())
                .user(user)
                .build();
        volunteeringRepository.save(volunteering);

        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(()-> new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.plusVolunteeringTime(Integer.parseInt(volunteeringCreateDto.time()));

        return VolunteeringInfo.from(volunteering);
    }

    public void deleteVolunteering(Long volunteeringId, String email) {
        Volunteering volunteering = volunteeringRepository.findById(volunteeringId).orElseThrow(() -> new CustomException(ExceptionCode.VOLUNTEERING_NOT_EXIST));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
        MyPage myPage = myPageRepository.findByUser(user).orElseThrow(()-> new CustomException(ExceptionCode.MY_PAGE_NOT_EXIST));
        myPage.minusVolunteeringTime(Integer.parseInt(volunteering.getTime()));

        if(!volunteering.getUser().getId().equals(user.getId())){
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS_CERTIFICATION);
        }
        String fileName = volunteering.getGenerateFileName();
        fileComponent.deleteFileFromFileSystem(fileName);


        volunteeringRepository.delete(volunteering);
    }
}
