package com.services.obssjobs.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.services.obssjobs.model.ObssJobsObject;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "JOB_POST")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class JobPost extends ObssJobsObject implements Serializable {

    @Column(name="title", nullable = false)
    private String title;

    /* Name of the city in which the candidate will work */
    @Column(name="location", nullable = false)
    private String location;

    @Column(name="is_active", nullable = false)
    private Boolean isActive;

    /* Name of the HR Expert who created the post */
    @Column(name="created_by", nullable = false)
    private String createdBy;

    /* Header image's url to be displayed on frontend */
    @Column(name="image_src", nullable = false)
    private String imageSource;

    @Column(name="job_description", nullable = false)
    private String jobDescription;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "activation_date", nullable = false)
    private Date activationDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name="expiration_date", nullable = false)
    private Date expirationDate;

    @OneToMany(mappedBy = "jobPost")
    List<JobRequirement> jobRequirements;

    @OneToMany(mappedBy = "jobPost")
    List<Application> applications;
}
