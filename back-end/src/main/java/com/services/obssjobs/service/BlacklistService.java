package com.services.obssjobs.service;

import com.services.obssjobs.model.entity.Blacklist;
import com.services.obssjobs.repository.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistService {

    private BlacklistRepository blacklistRepository;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    public Boolean isBlacklisted(String candidateId) {
        return blacklistRepository.getBlacklistStatusById(candidateId).isPresent();
    }

    public void save(Blacklist blacklistEntry) {
        this.blacklistRepository.save(blacklistEntry);
    }
}
