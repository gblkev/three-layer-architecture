package com.gebel.threelayerarchitecture.controller.test;

import org.springframework.test.context.DynamicPropertyRegistry;

public class TestContainersManager {

	private MysqlDatabaseContainer mysqlDatabaseContainer;
	
	public void startContainers() {
		mysqlDatabaseContainer.start();
	}
	
	public void stopContainers() {
		mysqlDatabaseContainer.stop();
	}
	
	public void setDynamicContainersConfiguration(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> mysqlDatabaseContainer.getJdbcUrl());
	}
	
}