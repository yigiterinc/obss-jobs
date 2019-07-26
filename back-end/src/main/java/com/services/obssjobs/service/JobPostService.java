package com.services.obssjobs.service;

import com.services.obssjobs.exception.NotFoundException;
import com.services.obssjobs.model.dto.HrAdminDto;
import com.services.obssjobs.model.dto.JobDetailsDto;
import com.services.obssjobs.model.dto.JobPostDto;
import com.services.obssjobs.model.entity.Application;
import com.services.obssjobs.model.entity.JobPost;
import com.services.obssjobs.repository.JobPostRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@NoArgsConstructor
public class JobPostService {

    private JobPostRepository jobPostRepository;

    @Autowired
    public JobPostService(JobPostRepository jobPostRepository) {
        this.jobPostRepository = jobPostRepository;
    }

    public JobPost findById(String id) {
        return jobPostRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<JobPost> getAll() {
        return jobPostRepository.findAll();
    }

    public void deleteById(String id) {
        jobPostRepository.deleteById(id);
    }

    public List<JobPostDto> getJobPostDtoList() {
        return jobPostRepository.getJobPostsDto();
    }

    public JobDetailsDto getJobDetailsDto(String jobId) {
        return jobPostRepository.getJobDetailsDto(jobId);
    }

    public List<Application> getApplicationsForJobByJobId(String jobId) {
        return jobPostRepository.getApplicationsForJobByJobId(jobId);
    }

    public List<HrAdminDto> getHrAdminDtoList() {
        return jobPostRepository.getHrAdminDtoList();
    }

    public void updateJobPostDetails(String jobPostId, String title, Boolean isActive,
                                     String jobDescription, Date activationDate, Date expirationDate) {

        JobPost oldPost = findById(jobPostId);

        JobPost jobPost = JobPost.builder().title(title)
                                            .imageSource(oldPost.getImageSource())
                                            .location(oldPost.getLocation())
                                            .createdBy(oldPost.getCreatedBy())
                                            .isActive(isActive)
                                            .jobDescription(jobDescription)
                                            .activationDate(activationDate)
                                            .expirationDate(expirationDate).build();

        jobPost.setId(oldPost.getId());
        save(jobPost);
    }

    public String createAndSaveJobPost(String title, String location, Boolean isActive, String createdBy,
                                       String imageSrc, String jobDescription,
                                       Date activationDate, Date expirationDate) {

        JobPost jobPost = this.createJobPost(title, location, isActive, createdBy, imageSrc,
                                            jobDescription, activationDate, expirationDate);

        save(jobPost);
        return jobPost.getId();
    }

    public void save(JobPost jobPost) {
        jobPostRepository.save(jobPost);
    }

    private JobPost createJobPost(String title, String location, Boolean isActive, String createdBy,
                                  String imageSrc, String jobDescription, Date activationDate, Date expirationDate) {

        return JobPost.builder()
                      .title(title)
                      .location(location)
                      .isActive(isActive)
                      .createdBy(createdBy)
                      .imageSource(imageSrc)
                      .jobDescription(jobDescription)
                      .activationDate(activationDate)
                      .expirationDate(expirationDate)
                      .build();
    }
}
