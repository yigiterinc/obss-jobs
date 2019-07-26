package com.services.obssjobs.model.entity.solr;

import com.services.obssjobs.model.entity.CandidateLinkedIn;
import lombok.*;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;

@Getter
@Setter
@Data
@NoArgsConstructor
@SolrDocument(solrCoreName = "obssjobs")
public class CandidateLinkedInSummary {

    public CandidateLinkedInSummary(String candidateId, String fullName, String email, String skill, Boolean blacklisted) {
        this.candidateId = candidateId;
        this.fullName = fullName;
        this.email = email;
        this.skill = skill;
        this.blacklisted = blacklisted;
    }

    public CandidateLinkedInSummary(CandidateLinkedIn candidateLinkedIn) {
        this.candidateId = candidateLinkedIn.getId();
        this.fullName = candidateLinkedIn.getFullName();
        this.email = candidateLinkedIn.getEmail();
        this.skill = "";
        this.blacklisted = false;
    }

    @Id
    String id;

    @Indexed(name = "candidateId", type = "string")
    String candidateId;

    @Indexed(name = "email", type = "string")
    String email;

    @Indexed(name = "fullName", type = "string")
    String fullName;

    @Indexed(name = "skill", type = "string")
    String skill;

    @Indexed(name = "blacklisted", type = "boolean")
    Boolean blacklisted;
}
