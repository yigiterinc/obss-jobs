package com.services.obssjobs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.services.obssjobs.model.ObssJobsObject;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LINKED_IN_SKILL")
public class LinkedInSkill extends ObssJobsObject {

    public LinkedInSkill(String skill) {
        this.skill = skill;
    }

    @Column(name = "SKILL", unique = true, nullable = false)
    private String skill;

    @JsonIgnore
    @ManyToMany(mappedBy = "linkedInSkillSet")
    private Set<CandidateLinkedIn> candidateLinkedInSet;
}
