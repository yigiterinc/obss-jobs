package com.services.obssjobs.controller;

import com.services.obssjobs.service.LinkedInSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/controller/linked-in-skills")
public class LinkedInSkillController {

    private LinkedInSkillService linkedInSkillService;

    @Autowired
    public LinkedInSkillController(LinkedInSkillService linkedInSkillService) {
        this.linkedInSkillService = linkedInSkillService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAll() {
        return new ResponseEntity<>(linkedInSkillService.findAll(), HttpStatus.OK);
    }
}
