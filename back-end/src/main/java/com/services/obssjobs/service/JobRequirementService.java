package com.services.obssjobs.service;

import com.services.obssjobs.model.dto.JobRequirementDto;
import com.services.obssjobs.model.entity.JobPost;
import com.services.obssjobs.model.entity.JobRequirement;
import com.services.obssjobs.exception.NotFoundException;
import com.services.obssjobs.repository.JobRequirementRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class JobRequirementService {

    private JobRequirementRepository jobRequirementRepository;

    private JobPostService jobPostService;

    @Autowired
    public JobRequirementService(JobRequirementRepository jobRequirementRepository,
                                 JobPostService jobPostService) {

        this.jobRequirementRepository = jobRequirementRepository;
        this.jobPostService = jobPostService;
    }

    public List<JobRequirementDto> getJobRequirementsByJobId(String jobId) {
        return jobRequirementRepository.getJobRequirementsByJobId(jobId);
    }

    public JobRequirement findById(String id) {
        return jobRequirementRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void save(JobRequirement jobRequirement) {
        jobRequirementRepository.save(jobRequirement);
    }

    private JobRequirement createJobRequirementFor(JobPost jobPost, String requirement) {

        return JobRequirement.builder().jobPost(jobPost)
                .requirement(requirement)
                .build();
    }


    public void saveJobRequirementByJobId(String jobId, String requirement) {
        JobPost jobPost = jobPostService.findById(jobId);

        JobRequirement jobRequirement = createJobRequirementFor(jobPost, requirement);
        save(jobRequirement);
    }
}
