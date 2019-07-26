package com.services.obssjobs.controller;

import com.services.obssjobs.exception.NotFoundException;
import com.services.obssjobs.model.dto.JobRequirementDto;
import com.services.obssjobs.model.entity.JobPost;
import com.services.obssjobs.model.entity.JobRequirement;
import com.services.obssjobs.service.JobPostService;
import com.services.obssjobs.service.JobRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/services/controller/job-requirements")
public class JobRequirementController {

    private JobRequirementService jobRequirementService;

    @Autowired
    public JobRequirementController(JobRequirementService jobRequirementService) {
        this.jobRequirementService = jobRequirementService;
    }

    @GetMapping
    public List<JobRequirementDto> getJobRequirementsByJobId(@RequestParam String jobId) {
        return jobRequirementService.getJobRequirementsByJobId(jobId);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveJobRequirementForJobByJobId(@RequestParam String jobId,
                                                             @RequestParam String requirement) {

        try {
            jobRequirementService.saveJobRequirementByJobId(jobId, requirement);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Transactional(rollbackOn = Exception.class)
    @PutMapping
    @RequestMapping("/update")
    public ResponseEntity<?> updateJobRequirementById(@RequestParam String requirementId,
                                                      @RequestParam String newRequirement) {

        JobRequirement jobRequirement = jobRequirementService.findById(requirementId);
        jobRequirement.setRequirement(newRequirement);
        jobRequirementService.save(jobRequirement);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}