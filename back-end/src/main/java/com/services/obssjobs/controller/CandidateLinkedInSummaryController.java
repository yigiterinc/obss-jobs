package com.services.obssjobs.controller;

import com.services.obssjobs.model.entity.solr.CandidateLinkedInSummary;
import com.services.obssjobs.service.solr.CandidateLinkedInSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/services/controller/search")
public class CandidateLinkedInSummaryController {

    CandidateLinkedInSummaryService candidateLinkedInSummaryService;

    @Autowired
    public CandidateLinkedInSummaryController(CandidateLinkedInSummaryService candidateLinkedInSummaryService) {
        this.candidateLinkedInSummaryService = candidateLinkedInSummaryService;
    }

    @GetMapping
    public Set<CandidateLinkedInSummary> search(@RequestParam String query) {
        return candidateLinkedInSummaryService.search(query);
    }

    @GetMapping("/get-all")
    public List<CandidateLinkedInSummary> getAllSummaries() {
        return candidateLinkedInSummaryService.getAllSummaries();
    }
}
