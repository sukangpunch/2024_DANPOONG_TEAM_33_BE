package com.onetry.spring.transcript.dto;


import org.springframework.web.multipart.MultipartFile;

public record TranscriptCreateDto(
        String transcriptName,

        MultipartFile transcriptFile
) {
}
