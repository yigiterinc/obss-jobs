package com.services.obssjobs.repository;

import com.services.obssjobs.model.dto.CandidateProfileDto;
import com.services.obssjobs.model.entity.CandidateLinkedIn;
import com.services.obssjobs.model.entity.LinkedInSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidateLinkedInRepository extends JpaRepository<CandidateLinkedIn, String> {

    @Override
    List<CandidateLinkedIn> findAll();

    @Override
    Optional<CandidateLinkedIn> findById(String s);

    Optional<CandidateLinkedIn> findByEmail(String email);

    @Query(
    "select new com.services.obssjobs.model.dto.CandidateProfileDto(" +
    "candidateLinkedIn.fullName, candidateLinkedIn.profilePhotoUrl) " +
    "from CandidateLinkedIn candidateLinkedIn " +
    "where candidateLinkedIn.id = :id"
    )
    CandidateProfileDto getCandidateProfileDto(@Param("id") String id);
}
