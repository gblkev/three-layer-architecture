package com.gebel.threelayerarchitecture.controller._test;

import org.springframework.test.context.DynamicPropertyRegistry;

import test.com.gebel.threelayerarchitecture.sandbox.container.TestContainers;

public class TestContainersManager {
	
	private static final String DB_MYSQL_VERSION = "8.0.11";
	private static final String DB_NAME = "cars_db";
	private static final String DB_USER = "test_user";
	private static final String DB_PASSWORD = "test_password";
	
	private TestContainers testContainers;
	
	public TestContainersManager() {
		TestContainers testContainers = new TestContainers();
		testContainers.initMysqlDatabaseContainerWithRandomPort(DB_MYSQL_VERSION, DB_NAME, DB_USER, DB_PASSWORD);
		this.testContainers = testContainers;
	}
	
	public void setDynamicContainersConfiguration(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> testContainers.getMysqlDatabaseTestContainer().getJdbcUrl());
	}
	
	public void startContainers() {
		testContainers.startContainers();
	}
	
	public void stopContainers() {
		testContainers.stopContainers();
	}

}