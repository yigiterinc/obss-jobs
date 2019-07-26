package com.services.obssjobs.controller;

import com.services.obssjobs.model.dto.HrAdminDto;
import com.services.obssjobs.model.dto.JobDetailsDto;
import com.services.obssjobs.model.dto.JobPostDto;
import com.services.obssjobs.model.entity.Application;
import com.services.obssjobs.model.entity.JobPost;
import com.services.obssjobs.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/services/controller/job-post")
public class JobPostController {

    private final JobPostService jobPostService;

    @Autowired
    public JobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    // Formats the dates that are transferred from this controller
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true, 10));
    }

    @GetMapping
    public List<JobPost> getAll() {
        return jobPostService.getAll();
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam String title, @RequestParam String location, @RequestParam Boolean isActive,
                       @RequestParam String createdBy, @RequestParam String imageSrc, @RequestParam String jobDescription,
                       @RequestParam Date activationDate, @RequestParam Date expirationDate) {

        String jobPostId = jobPostService.createAndSaveJobPost(title, location, isActive, createdBy,
                                                        imageSrc, jobDescription, activationDate, expirationDate);

        return new ResponseEntity<>(jobPostId, HttpStatus.OK);
    }

    @PutMapping("/update-details")
    @Transactional
    public ResponseEntity<?> updateJobPostDetails(@RequestParam String jobPostId, @RequestParam String title,
                                                  @RequestParam Boolean isActive, @RequestParam String jobDescription,
                                                  @RequestParam Date activationDate, @RequestParam Date expirationDate) {

        try {
            jobPostService.updateJobPostDetails(jobPostId, title, isActive,
                                                jobDescription, activationDate, expirationDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional(rollbackOn = Exception.class)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        jobPostService.deleteById(id);

        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/posts")
    public List<JobPostDto> getJobPostsDtoList() {
        return jobPostService.getJobPostDtoList();
    }

    @GetMapping("/job-details")
    public JobDetailsDto getJobDetailsDto(@RequestParam String jobId) {
        return jobPostService.getJobDetailsDto(jobId);
    }

    @GetMapping("/applications")
    public List<Application> getApplicationsForJobByJobId(@RequestParam String jobId) {
        return jobPostService.getApplicationsForJobByJobId(jobId);
    }

    @GetMapping("/hr-admin")
    public List<HrAdminDto> getHrAdminDtoList() {
        return jobPostService.getHrAdminDtoList();
    }
}
