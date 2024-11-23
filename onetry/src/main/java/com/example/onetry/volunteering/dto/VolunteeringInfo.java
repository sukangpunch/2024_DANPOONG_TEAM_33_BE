package com.example.onetry.volunteering.dto;

import com.example.onetry.volunteering.entity.Volunteering;
import lombok.Builder;

@Builder
public record VolunteeringInfo(
        Long id,
        String time,
        String volunteeringFileName,
        String volunteeringFilePath
) {
    public static VolunteeringInfo from(Volunteering volunteering){
        return VolunteeringInfo.builder()
                .id(volunteering.getId())
                .time(volunteering.getTime())
                .volunteeringFileName(volunteering.getVolunteeringFileName())
                .volunteeringFilePath(volunteering.getVolunteeringPath())
                .build();
    }
}
