package com.services.obssjobs.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JobDetailsDto {

    private String id, title, description, location;
    private Date activationDate, expirationDate;
    private Boolean isActive;

    public JobDetailsDto(String id, String title, String description, String location,
                         Date activationDate, Date expirationDate, Boolean isActive) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.activationDate = activationDate;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
    }
}
