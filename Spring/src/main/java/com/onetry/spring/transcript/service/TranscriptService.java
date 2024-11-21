package com.onetry.spring.transcript.service;


import com.onetry.spring.common.WriteFileToFileSystemResult;
import com.onetry.spring.component.file.FileComponent;
import com.onetry.spring.exception.CustomException;
import com.onetry.spring.exception.ExceptionCode;
import com.onetry.spring.transcript.dto.TranscriptCreateDto;
import com.onetry.spring.transcript.dto.TranscriptInfo;
import com.onetry.spring.transcript.entity.Transcript;
import com.onetry.spring.transcript.repository.TranscriptRepository;
import com.onetry.spring.user.entity.User;
import com.onetry.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TranscriptService {

    private final TranscriptRepository transcriptRepository;
    private final UserRepository userRepository;
    private final FileComponent fileComponent;

    public List<TranscriptInfo> getAllTranscript(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
        List<Transcript> transcripts = transcriptRepository.findByUser(user);

        return transcripts.stream().map(TranscriptInfo::from).toList();
    }

    public TranscriptInfo addTranscript(String email, TranscriptCreateDto transcriptCreateDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));
        WriteFileToFileSystemResult writeFileToFileSystemResult = fileComponent.uploadFromFileSystem(transcriptCreateDto.transcriptFile().getOriginalFilename(), transcriptCreateDto.transcriptFile());

        Transcript transcript = Transcript.builder()
                .transcriptName(transcriptCreateDto.transcriptName())
                .generateFileName(writeFileToFileSystemResult.generateName())
                .transcriptPath(writeFileToFileSystemResult.filePath())
                .user(user)
                .build();

        transcriptRepository.save(transcript);
        return TranscriptInfo.from(transcript);
    }

    public void deleteTranscript(Long transcriptId, String email) {
        Transcript transcript = transcriptRepository.findById(transcriptId).orElseThrow(() -> new CustomException(ExceptionCode.TRANSCRIPT_NOT_EXIST));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_EXIST));

        if(!transcript.getUser().getId().equals(user.getId())){
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS_CERTIFICATION);
        }
        String fileName = transcript.getGenerateFileName();
        fileComponent.deleteFileFromFileSystem(fileName);
        transcriptRepository.delete(transcript);
    }
}
