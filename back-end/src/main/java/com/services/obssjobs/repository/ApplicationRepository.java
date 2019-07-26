package com.services.obssjobs.repository;

import com.services.obssjobs.model.dto.CandidateApplicationsDto;
import com.services.obssjobs.model.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, String> {

    Optional<Application> findById(String id);

    @Override
    List<Application> findAll();

    @Override
    void deleteById(String id);

    @Query(
    "select jp.title, a.applicationStatus " +
    "from Application a " +
    "inner join JobPost jp " +
    "where jp.id = :id and a.jobPost = jp"
    )
    List<Application> getJobApplicationsById(@Param("id") String id);

    @Query(
    "select new com.services.obssjobs.model.dto.CandidateApplicationsDto( " +
    "a.applicationStatus, a.jobPost.title, a.jobPost.location, " +
    "a.jobPost.id, a.applicationDate) " +
    "from Application a " +
    "where a.candidateLinkedIn.id = :linkedInId"
    )
    List<CandidateApplicationsDto> getCandidateApplicationsDtoListByLinkedInId(@Param("linkedInId") String linkedInId);
}
