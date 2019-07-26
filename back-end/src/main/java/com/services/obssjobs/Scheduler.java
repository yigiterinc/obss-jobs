package com.services.obssjobs;

import com.services.obssjobs.model.entity.JobPost;
import com.services.obssjobs.service.JobPostService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
@Transactional
@Setter
@Getter
public class Scheduler {

    private JobPostService jobPostService;

    @Autowired
    public Scheduler(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @Scheduled(cron = "0 0 */6 * * *")
    protected void updateJobPostStatuses() {
        List<JobPost> allJobPosts = jobPostService.getAll();
        Date today = new Date();

        for (JobPost jobPost : allJobPosts) {
            boolean activationStatusChanged = false;

            if (today.after(jobPost.getActivationDate()) && today.before(jobPost.getExpirationDate())) {
                jobPost.setIsActive(true);
                activationStatusChanged = true;
            } else if (today.after(jobPost.getExpirationDate())) {
                jobPost.setIsActive(false);
                activationStatusChanged = true;
            }

            if (activationStatusChanged) {
                jobPostService.save(jobPost);
            }
        }
    }
}
