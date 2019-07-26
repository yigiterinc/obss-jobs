package com.services.obssjobs.controller;

import com.services.obssjobs.exception.LinkedInAccessTokenExpiredException;
import com.services.obssjobs.model.dto.CandidateProfileDto;
import com.services.obssjobs.model.entity.Application;
import com.services.obssjobs.model.entity.Blacklist;
import com.services.obssjobs.model.entity.CandidateLinkedIn;
import com.services.obssjobs.exception.NotFoundException;
import com.services.obssjobs.service.BlacklistService;
import com.services.obssjobs.service.CandidateLinkedInService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.*;


@RestController
@RequestMapping("/api/services/controller/linked-in")
public class CandidateLinkedInController {

    private CandidateLinkedInService candidateLinkedInService;

    @Autowired
    public CandidateLinkedInController(CandidateLinkedInService candidateLinkedInService) {
        this.candidateLinkedInService = candidateLinkedInService;
    }

    @GetMapping("/candidate-profile")
    public CandidateProfileDto getCandidateProfileDto(@RequestParam String id) {
        return this.candidateLinkedInService.getCandidateProfileDto(id);
    }

    @GetMapping("/is-blacklisted")
    public Boolean getBlacklistStatus(@RequestParam String candidateId) {
        return this.candidateLinkedInService.isBlacklisted(candidateId);
    }

    @PostMapping("/blacklist")
    public ResponseEntity<String> addCandidateToBlacklist(@RequestParam String candidateId, @RequestParam String reason) {
        try {
            candidateLinkedInService.addCandidateToBlackList(candidateId, reason);
            return new ResponseEntity<>(candidateId, HttpStatus.NOT_FOUND);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/exchange-token")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<String> exchangeLinkedInToken(@RequestParam("code") String authorizationCode) throws Exception {
        try {
            String token = candidateLinkedInService.exchangeLinkedInToken(authorizationCode);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (NotFoundException e1) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        } catch (LinkedInAccessTokenExpiredException e2) {
            return new ResponseEntity<>("", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("{candidateId}/get-skills")
    public ResponseEntity<List<String>> getSkills(@PathVariable String candidateId) {
        try {
            return new ResponseEntity<>(candidateLinkedInService.getSkills(candidateId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{candidateId}/save-skills")
    public ResponseEntity<String> saveSkills(@PathVariable String candidateId, @RequestBody List<String> skills) {
        try {
            candidateLinkedInService.saveSkills(candidateId, skills);
            return new ResponseEntity<>(candidateId, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }
}
