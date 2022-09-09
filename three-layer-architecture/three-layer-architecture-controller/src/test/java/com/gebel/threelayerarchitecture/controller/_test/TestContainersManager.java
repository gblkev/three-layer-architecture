package com.gebel.threelayerarchitecture.controller._test;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import org.springframework.test.context.DynamicPropertyRegistry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestContainersManager {

	private MysqlDatabaseContainer mysqlDatabaseContainer = new MysqlDatabaseContainer();
	
	public void startContainers() {
		LOGGER.info("Starting containers in parallel...");
		Instant start = Instant.now();
		Stream.of(mysqlDatabaseContainer)
			.forEach(GenericContainer::start);
		mysqlDatabaseContainer.start();
		Instant end = Instant.now();
		LOGGER.info("Containers started in {}", Duration.between(start, end));
	}
	
	public void stopContainers() {
		LOGGER.info("Stopping containers...");
		mysqlDatabaseContainer.stop();
	}
	
	public void setDynamicContainersConfiguration(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> mysqlDatabaseContainer.getJdbcUrl());
	}
	
}