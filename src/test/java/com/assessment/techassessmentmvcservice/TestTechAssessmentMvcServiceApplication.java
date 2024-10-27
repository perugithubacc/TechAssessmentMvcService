package com.assessment.techassessmentmvcservice;

import org.springframework.boot.SpringApplication;

public class TestTechAssessmentMvcServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(TechAssessmentMvcServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
