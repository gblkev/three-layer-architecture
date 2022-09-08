package com.gebel.threelayerarchitecture.controller._test;

import org.springframework.test.context.DynamicPropertyRegistry;

public class TestContainersManager {

	private MysqlDatabaseContainer mysqlDatabaseContainer = new MysqlDatabaseContainer();
	
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