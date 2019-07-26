package com.services.obssjobs.model.entity;

import com.services.obssjobs.model.ObssJobsObject;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="CANDIDATE_LINKED_IN")
public class CandidateLinkedIn extends ObssJobsObject {

    @Column(name="FULL_NAME")
    private String fullName;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name="PHOTO_URL")
    private String profilePhotoUrl;

    @OneToMany(mappedBy = "candidateLinkedIn")
    private List<Application> applications;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "HAS_LINKED_IN_SKILL",
            joinColumns = {@JoinColumn(name = "LINKED_IN_ID")},
            inverseJoinColumns = {@JoinColumn(name = "LINKED_IN_SKILL_ID")}
    )
    @OrderBy("skill ASC")
    private Set<LinkedInSkill> linkedInSkillSet;
}
