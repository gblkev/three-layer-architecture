package com.gebel.threelayerarchitecture.sandbox.component;

import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.containers.MySQLContainer;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MysqlDatabaseContainer implements GenericContainer {

	private static final int CONTAINER_MAPPED_PORT = 3306;
	private static final String INIT_SCRIPT_PATH = "create-mysql-database.sql";
	
	@Value("${db.mysql.version}")
	private String mysqlVersion;
	
	@Value("${db.name}")
	private String dbName;
	
	@Value("${db.port}")
	private int dbPort;
	
	@Value("${db.user}")
	private String dbUser;
	
	@Value("${db.password}")
	private String dbPassword;
	
	private MySQLContainer<?> container;
	
	@Override
	@SuppressWarnings("resource") // Resource closed by "stop()"
	public void start() {
		container = (MySQLContainer<?>) new MySQLContainer<>("mysql:" + mysqlVersion)
			.withDatabaseName(dbName)
			.withUsername(dbUser)
			.withPassword(dbPassword)
			.withExposedPorts(CONTAINER_MAPPED_PORT)
			.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
				new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(dbPort), new ExposedPort(CONTAINER_MAPPED_PORT)))))
			.withInitScript(INIT_SCRIPT_PATH);
		LOGGER.info("Starting MySQL database...");
		container.start();
	}

	@Override
	public void stop() {
		container.stop();
	}

}