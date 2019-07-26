package com.services.obssjobs.controller;

import com.services.obssjobs.model.dto.CandidateApplicationsDto;
import com.services.obssjobs.model.entity.Application;
import com.services.obssjobs.model.entity.CandidateLinkedIn;
import com.services.obssjobs.model.entity.JobPost;
import com.services.obssjobs.exception.ApplicationAlreadyExistsException;
import com.services.obssjobs.exception.NotFoundException;
import com.services.obssjobs.service.ApplicationService;
import com.services.obssjobs.service.CandidateLinkedInService;
import com.services.obssjobs.service.JobPostService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@NoArgsConstructor
@RequestMapping("/api/services/controller/application")
public class ApplicationController {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam String userId, @RequestParam String jobPostId) {

        try {
            applicationService.saveApplicationByUserAndJobPost(userId, jobPostId);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        } catch (ApplicationAlreadyExistsException e) {
            return new ResponseEntity<>("Application already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public List<Application> getAll() {
        return applicationService.getAll();
    }

    @GetMapping("/{id}")
    public List<Application> getJobApplicationsByJobId(@PathVariable String id) {
        return applicationService.getJobApplicationsById(id);
    }

    @Transactional(rollbackOn = Exception.class)
    @DeleteMapping("/delete/{applicationId}")
    public @ResponseBody void delete(@PathVariable String applicationId) {
        applicationService.deleteById(applicationId);
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("/update-status")
    public ResponseEntity<?> updateApplicationStatus(@RequestParam String id, @RequestParam String applicationStatus) {
        try {
            applicationService.updateApplicationStatus(id, applicationStatus);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        }
    }

    @GetMapping("/candidate-applications")
    public List<CandidateApplicationsDto> getCandidateApplicationsDtoListByLinkedInId(@RequestParam String linkedInId) {
        return applicationService.getCandidateApplicationsDtoListByLinkedInId(linkedInId);
    }
}
