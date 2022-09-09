package com.gebel.threelayerarchitecture.sandbox.component;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class ContainersManager {

	private MysqlDatabaseContainer mysqlDatabaseContainer = new MysqlDatabaseContainer();
	
	@PostConstruct
	void startContainers() {
		LOGGER.info("Starting containers in parallel...");
		Instant start = Instant.now();
		Stream.of(mysqlDatabaseContainer)
			.forEach(GenericContainer::start);
		mysqlDatabaseContainer.start();
		Instant end = Instant.now();
		LOGGER.info("Containers started in {}", Duration.between(start, end));
	}
	
	@PreDestroy
	void stopContainers() {
		LOGGER.info("Stopping containers...");
		mysqlDatabaseContainer.stop();
	}
	
}