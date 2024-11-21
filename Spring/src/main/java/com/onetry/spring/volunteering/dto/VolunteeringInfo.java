package com.onetry.spring.volunteering.dto;

import com.onetry.spring.volunteering.entity.Volunteering;
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
