package com.services.obssjobs.service.solr;

import com.services.obssjobs.model.entity.CandidateLinkedIn;
import com.services.obssjobs.model.entity.solr.CandidateLinkedInSummary;
import com.services.obssjobs.repository.solr.CandidateLinkedInSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CandidateLinkedInSummaryService {

    private CandidateLinkedInSummaryRepository candidateLinkedInSummaryRepository;

    @Autowired
    public CandidateLinkedInSummaryService(CandidateLinkedInSummaryRepository candidateLinkedInSummaryRepository) {
        this.candidateLinkedInSummaryRepository = candidateLinkedInSummaryRepository;
    }

    public List<CandidateLinkedInSummary> getAllSummaries() {
        return candidateLinkedInSummaryRepository.getAllSummaries();
    }

    public Set<CandidateLinkedInSummary> search(String query) {
        Set<CandidateLinkedInSummary> resultSet;
        List<CandidateLinkedInSummary> findByAnyFieldResult = new ArrayList<>();

        try {
            findByAnyFieldResult = candidateLinkedInSummaryRepository
                    .findIfAnyFieldMatches(query);
        } catch (Exception e) {
            findByAnyFieldResult = candidateLinkedInSummaryRepository
                    .findIfAnyFieldMatchesExcludeSkills(query);
        } finally {
            List<CandidateLinkedInSummary> findByEmailResult = candidateLinkedInSummaryRepository.findByEmail(query);
            resultSet = new HashSet<>(findByAnyFieldResult);
            resultSet.addAll(findByEmailResult);
        }

        return resultSet;
    }

    public void save(String id, String fullName, String email, String skill, Boolean blacklisted) {
        CandidateLinkedInSummary candidateLinkedInSummary = new CandidateLinkedInSummary(id, fullName, email,
                                                                                          skill, blacklisted);
        candidateLinkedInSummaryRepository.save(candidateLinkedInSummary);
    }

    public void save(CandidateLinkedIn candidateLinkedIn) {
        CandidateLinkedInSummary candidateLinkedInSummary = new CandidateLinkedInSummary(candidateLinkedIn);

        candidateLinkedInSummaryRepository.save(candidateLinkedInSummary);
    }

}
