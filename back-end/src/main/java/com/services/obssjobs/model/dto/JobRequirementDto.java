package com.services.obssjobs.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRequirementDto {

    public String id;
    public String requirement;

    public JobRequirementDto(String id, String requirement) {
        this.id = id;
        this.requirement = requirement;
    }
}
