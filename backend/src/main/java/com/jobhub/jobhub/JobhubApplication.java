package com.jobhub.jobhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobhubApplication.class, args);
	}

}
