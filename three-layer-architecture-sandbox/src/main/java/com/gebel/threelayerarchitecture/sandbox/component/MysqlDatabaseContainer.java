package com.gebel.threelayerarchitecture.sandbox.component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;
import org.testcontainers.containers.MySQLContainer;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MysqlDatabaseContainer {

	private static final String MYSQL_VERSION = "8.0.11";
	private static final String DB_NAME = "cars_db";
	private static final String DB_USER = "test_user";
	private static final String DB_PASSWORD = "test_password";
	private static final int DB_PORT = 3306;
	private static final String INIT_SCRIPT_PATH = "create-mysql-database.sql";
	
	private static final MySQLContainer<?> CONTAINER = (MySQLContainer<?>) new MySQLContainer<>("mysql:" + MYSQL_VERSION)
		.withDatabaseName(DB_NAME)
		.withUsername(DB_USER)
		.withPassword(DB_PASSWORD)
		.withExposedPorts(DB_PORT)
		.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
			new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(DB_PORT), new ExposedPort(DB_PORT)))))
		.withInitScript(INIT_SCRIPT_PATH);

	@PostConstruct
	public void start() {
		LOGGER.info("Starting MySQL database...");
		CONTAINER.start();
	}

	@PreDestroy
	public void stop() {
		CONTAINER.stop();
	}

}