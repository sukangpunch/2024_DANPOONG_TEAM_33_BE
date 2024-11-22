package com.example.onetry.volunteering.dto;

import org.springframework.web.multipart.MultipartFile;

public record VolunteeringCreateDto(
        String time,
        String volunteeringFileName,
        MultipartFile volunteeringFile
) {
}
