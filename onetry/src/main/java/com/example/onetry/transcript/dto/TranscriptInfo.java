package com.example.onetry.transcript.dto;

import com.example.onetry.transcript.entity.Transcript;
import lombok.Builder;

@Builder
public record TranscriptInfo(
        Long id,
        String transcriptName,
        String transcriptPath
) {
    public static TranscriptInfo from(Transcript transcript){
        return TranscriptInfo.builder()
                .id(transcript.getId())
                .transcriptName(transcript.getTranscriptName())
                .transcriptPath(transcript.getTranscriptPath())
                .build();
    }
}
