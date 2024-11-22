package com.example.onetry.transcript.service;

import com.example.onetry.certification.entity.Certification;
import com.example.onetry.common.WriteFileToFileSystemResult;
import com.example.onetry.component.file.FileComponent;
import com.example.onetry.exception.CustomException;
import com.example.onetry.exception.ExceptionCode;
import com.example.onetry.transcript.dto.TranscriptCreateDto;
import com.example.onetry.transcript.dto.TranscriptInfo;
import com.example.onetry.transcript.entity.Transcript;
import com.example.onetry.transcript.repository.TranscriptRepository;
import com.example.onetry.user.entity.User;
import com.example.onetry.user.repository.UserRepository;
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
