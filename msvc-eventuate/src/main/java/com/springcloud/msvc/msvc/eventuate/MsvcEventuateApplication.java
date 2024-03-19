package com.springcloud.msvc.msvc.eventuate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Patterns used: Transactional outbox pattern
 *
 * This microservice was build in order to schedule each 10 seconds read de database to read
 * the outbox table and then process all the records which are pending to be processed
 * and then publish them in a kafka topic
 */
@SpringBootApplication
@EnableScheduling
public class MsvcEventuateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcEventuateApplication.class, args);
	}

}
