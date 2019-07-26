package com.services.obssjobs.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CandidateProfileDto {

    private String fullName;
    private String imageUrl;

    public CandidateProfileDto(String fullName, String imageUrl) {
        this.fullName = fullName;
        this.imageUrl = imageUrl;
    }
}
