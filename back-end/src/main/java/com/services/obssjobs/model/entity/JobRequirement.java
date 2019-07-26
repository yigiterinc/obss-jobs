package com.services.obssjobs.model.entity;

import com.services.obssjobs.model.ObssJobsObject;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "JOB_REQUIREMENT")
public class JobRequirement extends ObssJobsObject {

    @Column(name = "requirement", unique = true, nullable = false)
    private String requirement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", referencedColumnName = "id")
    JobPost jobPost;
}
