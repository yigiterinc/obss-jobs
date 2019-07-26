package com.services.obssjobs.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JobPostDto {

    private String id, title, location, imageSrc, description;
    private Date activationDate, expirationDate;

    public JobPostDto(String id, String title, String location, String imageSrc,
                      String description, Date activationDate, Date expirationDate) {

        this.id = id;
        this.title = title;
        this.location = location;
        this.imageSrc = imageSrc;
        this.description = description;
        this.activationDate = activationDate;
        this.expirationDate = expirationDate;
    }
}
