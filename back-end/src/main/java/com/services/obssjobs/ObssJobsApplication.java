package com.services.obssjobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ObssJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObssJobsApplication.class, args);
	}
}
