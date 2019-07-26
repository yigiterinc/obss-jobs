package com.services.obssjobs.repository;

import com.services.obssjobs.model.entity.LinkedInSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedInSkillRepository extends JpaRepository<LinkedInSkill, String> {

    LinkedInSkill findBySkill(String skill);
}
