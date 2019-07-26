package com.services.obssjobs.service;

import com.services.obssjobs.exception.ApplicationAlreadyExistsException;
import com.services.obssjobs.exception.NotFoundException;
import com.services.obssjobs.mail.MailService;
import com.services.obssjobs.model.dto.CandidateApplicationsDto;
import com.services.obssjobs.model.entity.Application;
import com.services.obssjobs.model.entity.CandidateLinkedIn;
import com.services.obssjobs.model.entity.JobPost;
import com.services.obssjobs.repository.ApplicationRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class ApplicationService {

    private ApplicationRepository applicationRepository;
    private MailService mailService;

    private CandidateLinkedInService candidateLinkedInService;
    private JobPostService jobPostService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, MailService mailService,
                              CandidateLinkedInService candidateLinkedInService, JobPostService jobPostService) {

        this.applicationRepository = applicationRepository;
        this.mailService = mailService;
        this.candidateLinkedInService = candidateLinkedInService;
        this.jobPostService = jobPostService;
    }

    public void save(Application application) {
        applicationRepository.save(application);
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public List<Application> getJobApplicationsById(String id) {
        return applicationRepository.getJobApplicationsById(id);
    }

    public List<Application> getAll() {
        return applicationRepository.findAll();
    }

    public void deleteById(String id) {
        applicationRepository.deleteById(id);
    }

    public Application findById(String id) {
        return applicationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<CandidateApplicationsDto> getCandidateApplicationsDtoListByLinkedInId(String linkedInId) {
        return applicationRepository.getCandidateApplicationsDtoListByLinkedInId(linkedInId);
    }

    public void updateApplicationStatus(String id, String applicationStatus) {
        Application application = this.findById(id);

        application.setApplicationStatus(applicationStatus);
        save(application);

        sendStatusChangeEmail(application.getCandidateLinkedIn().getEmail(), application.getJobPost().getTitle(),
                              application.getApplicationStatus());
    }

    private void sendStatusChangeEmail(String to, String jobTitle, String status) {
        mailService.sendStatusChangeEmail(to, jobTitle, status);
    }

    public void saveApplicationByUserAndJobPost(String userId, String jobPostId) {
        CandidateLinkedIn candidateLinkedIn = candidateLinkedInService.getCandidateLinkedInById(userId);
        JobPost jobPost = jobPostService.findById(jobPostId);

        if (jobPost == null)
            throw new NotFoundException();
        else if (hasCandidateAppliedForThisJobBefore(jobPostId, candidateLinkedIn))
            throw new ApplicationAlreadyExistsException();

        createApplicationAndSave(candidateLinkedIn, jobPost);
    }

    private boolean hasCandidateAppliedForThisJobBefore(@RequestParam String jobPostId,
                                                        CandidateLinkedIn candidateLinkedIn) {

        List<Application> applicationsToSameJob = candidateLinkedIn.getApplications().stream()
                .filter(application -> application.getJobPost().getId()
                        .equals(jobPostId)).collect(Collectors.toList());

        return !applicationsToSameJob.isEmpty();
    }

    private void createApplicationAndSave(CandidateLinkedIn candidateLinkedIn, JobPost jobPost) {
        Application application = Application.builder()
                .candidateLinkedIn(candidateLinkedIn)
                .applicationStatus("Awaiting")
                .applicationDate(new Date())
                .jobPost(jobPost)
                .build();

        save(application);
    }
}
