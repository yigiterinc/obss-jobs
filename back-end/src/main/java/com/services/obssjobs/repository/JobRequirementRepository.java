package com.services.obssjobs.repository;

import com.services.obssjobs.model.dto.JobRequirementDto;
import com.services.obssjobs.model.entity.JobRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRequirementRepository extends JpaRepository<JobRequirement, String> {

    @Query(
    "select new com.services.obssjobs.model.dto.JobRequirementDto( " +
    "jr.id, jr.requirement) " +
    "from JobRequirement jr " +
    "where jr.jobPost.id = :jobId"
    )
    List<JobRequirementDto> getJobRequirementsByJobId(@Param("jobId") String jobId);

    Optional<JobRequirement> findById(String s);
}
