package com.services.obssjobs.repository;

import com.services.obssjobs.model.dto.HrAdminDto;
import com.services.obssjobs.model.dto.JobDetailsDto;
import com.services.obssjobs.model.dto.JobPostDto;
import com.services.obssjobs.model.entity.Application;
import com.services.obssjobs.model.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Integer> {

    @Override
    List<JobPost> findAll();

    Optional<JobPost> findById(String id);

    void deleteById(String id);

    @Query(
    "select new com.services.obssjobs.model.dto.JobPostDto(" +
    "jp.id, jp.title, jp.location, jp.imageSource, " +
    "jp.jobDescription, jp.activationDate, jp.expirationDate) " +
    "from JobPost jp " +
    "where jp.isActive = true"
    )
    List<JobPostDto> getJobPostsDto();

    @Query(
    "select new com.services.obssjobs.model.dto.JobDetailsDto(" +
    "jp.id, jp.title, jp.jobDescription, jp.location, " +
    "jp.activationDate, jp.expirationDate, jp.isActive)" +
    "from JobPost jp " +
    "where jp.id = :jobId"
    )
    JobDetailsDto getJobDetailsDto(@Param("jobId") String jobId);

   @Query(
   "select new com.services.obssjobs.model.dto.HrAdminDto( " +
   "jp.id, jp.title, jp.jobDescription, jp.imageSource, " +
   "jp.activationDate, jp.expirationDate, jp.applications.size) " +
   "from JobPost jp "
   )
   List<HrAdminDto> getHrAdminDtoList();

   @Query(
   "select jp.applications " +
   "from JobPost jp " +
   "where jp.id = :jobId"
   )
   List<Application> getApplicationsForJobByJobId(@Param("jobId") String jobId);
}
