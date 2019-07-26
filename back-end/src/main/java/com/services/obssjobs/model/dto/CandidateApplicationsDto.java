package com.services.obssjobs.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CandidateApplicationsDto {

    String applicationStatus, jobPostTitle, jobPostId, location;
    Date applicationDate;

    public CandidateApplicationsDto(String applicationStatus, String jobPostTitle, String location,
                                    String jobPostId, Date applicationDate) {

        this.applicationStatus = applicationStatus;
        this.jobPostTitle = jobPostTitle;
        this.location = location;
        this.jobPostId = jobPostId;
        this.applicationDate = applicationDate;
    }
}
