package com.services.obssjobs.service;

import com.services.obssjobs.model.entity.LinkedInSkill;
import com.services.obssjobs.repository.LinkedInSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LinkedInSkillService {

    private LinkedInSkillRepository linkedInSkillRepository;

    @Autowired
    public LinkedInSkillService(LinkedInSkillRepository linkedInSkillRepository) {
        this.linkedInSkillRepository = linkedInSkillRepository;
    }

    public List<String> findAll() {
        List<LinkedInSkill> linkedInSkillList = linkedInSkillRepository.findAll();
        return linkedInSkillList.stream().map(LinkedInSkill::getSkill).collect(Collectors.toList());
    }

    Set<LinkedInSkill> getLinkedInSkillSetBySkills(List<String> skills) {
        return new HashSet<>(skills.stream().map(this::getOrCreateBySkill).collect(Collectors.toList()));
    }

    private LinkedInSkill getOrCreateBySkill(String skill) {
        LinkedInSkill linkedInSkill = linkedInSkillRepository.findBySkill(skill);
        return linkedInSkill != null ? linkedInSkill : new LinkedInSkill(skill);
    }
}
