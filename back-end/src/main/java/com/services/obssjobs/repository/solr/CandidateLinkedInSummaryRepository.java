package com.services.obssjobs.repository.solr;

import com.services.obssjobs.model.entity.solr.CandidateLinkedInSummary;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface CandidateLinkedInSummaryRepository extends SolrCrudRepository<CandidateLinkedInSummary, String> {

    @Query(value = "*:*")
    List<CandidateLinkedInSummary> getAllSummaries();

    @Query
    List<CandidateLinkedInSummary> findByEmail(String fullName);

    @Query("fullName:*?0* OR email:*?0* OR skill:*?0*")
    List<CandidateLinkedInSummary> findIfAnyFieldMatches(String text);

    @Query("fullName:*?0* OR email:*?0*")
    List<CandidateLinkedInSummary> findIfAnyFieldMatchesExcludeSkills(String text);
}
