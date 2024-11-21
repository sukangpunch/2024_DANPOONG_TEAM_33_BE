package com.onetry.spring.volunteering.service;

import com.onetry.spring.common.WriteFileToFileSystemResult;
import com.onetry.spring.component.file.FileComponent;
import com.onetry.spring.exception.CustomException;
import com.onetry.spring.exception.ExceptionCode;
import com.onetry.spring.user.entity.User;
import com.onetry.spring.user.repository.UserRepository;
import com.onetry.spring.volunteering.dto.VolunteeringCreateDto;
import com.onetry.spring.volunteering.dto.VolunteeringInfo;
import com.onetry.spring.volunteering.entity.Volunteering;
import com.onetry.spring.volunteering.repository.VolunteeringRepository;
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
        return VolunteeringInfo.from(volunteering);
    }

    public void deleteVolunteering(Long volunteeringId, String email) {
        Volunteering volunteering = volunteeringRepository.findById(volunteeringId).orElseThrow(() -> new CustomException(ExceptionCode.VOLUNTEERING_NOT_EXIST));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));

        if(!volunteering.getUser().getId().equals(user.getId())){
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS_CERTIFICATION);
        }
        String fileName = volunteering.getGenerateFileName();
        fileComponent.deleteFileFromFileSystem(fileName);
        volunteeringRepository.delete(volunteering);
    }
}
