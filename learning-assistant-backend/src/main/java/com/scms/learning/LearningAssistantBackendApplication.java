package com.scms.learning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.scms.learning.mapper")
@SpringBootApplication
public class LearningAssistantBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningAssistantBackendApplication.class, args);
	}
}