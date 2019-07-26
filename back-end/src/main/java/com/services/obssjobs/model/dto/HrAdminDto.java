package com.services.obssjobs.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class HrAdminDto {

    private String id, title, description, imageSrc;
    private Date activationDate, expirationDate;
    private Integer numberOfApplications;

    public HrAdminDto(String id, String title, String description,
                      String imageSrc, Date activationDate, Date expirationDate,
                      Integer numberOfApplications) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.imageSrc = imageSrc;
        this.activationDate = activationDate;
        this.expirationDate = expirationDate;
        this.numberOfApplications = numberOfApplications;
    }
}
