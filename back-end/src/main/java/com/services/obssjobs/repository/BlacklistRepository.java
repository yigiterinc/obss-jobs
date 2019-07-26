package com.services.obssjobs.repository;

import com.services.obssjobs.model.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, String> {

    @Query(
    "select b " +
    "from Blacklist b " +
    "where b.candidateLinkedIn.id = :candidateId"
    )
    Optional<Blacklist> getBlacklistStatusById(@Param("candidateId") String candidateId);
}
