package com.example.onetry.common;

import lombok.Builder;
import org.springframework.core.io.Resource;

@Builder
public record FileDownload(
        Resource resource,
        String fileName
) {
    public static FileDownload of(Resource resource, String fileName){
        return FileDownload.builder()
                .fileName(fileName)
                .resource(resource)
                .build();
    }
}
