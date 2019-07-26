package com.services.obssjobs.controller;

import com.services.obssjobs.service.HrExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/services/controller/hr-expert")
public class HrExpertController {

    private final HrExpertService hrExpertService;

    @Autowired
    public HrExpertController(HrExpertService hrExpertService) {
        this.hrExpertService = hrExpertService;
    }

    @PostMapping("/authenticate")
    public boolean authenticate(@RequestParam String name, @RequestParam String password) {
        // To avoid case sensitivity in name
        String nameInLowercase = name.toLowerCase();

        return hrExpertService.authenticate(nameInLowercase, password);
    }
}
