package com.services.obssjobs.model.entity;

import com.services.obssjobs.model.ObssJobsObject;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="BLACKLIST")
public class Blacklist extends ObssJobsObject implements Serializable {

    @Column
    private String reason;

    @OneToOne
    @MapsId
    private CandidateLinkedIn candidateLinkedIn;
}
