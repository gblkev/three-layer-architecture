package com.gebel.threelayerarchitecture.controller._test;

import org.testcontainers.containers.MySQLContainer;

class MysqlDatabaseContainer implements GenericContainer {

	private static final String MYSQL_VERSION = "mysql:8.0.11";
	private static final String DB_NAME = "cars_db";
	private static final String DB_USER = "test_user";
	private static final String DB_PASSWORD = "test_password";
	private static final String INIT_SCRIPT_PATH = "db/create-mysql-database.sql";
	
	private MySQLContainer<?> container;
	
	@SuppressWarnings("resource")
	MysqlDatabaseContainer() {
		container = new MySQLContainer<>(MysqlDatabaseContainer.MYSQL_VERSION)
			.withDatabaseName(DB_NAME)
			.withUsername(DB_USER)
			.withPassword(DB_PASSWORD)
			.withInitScript(INIT_SCRIPT_PATH);
	}
	
	@Override
	public void start() {
		container.start();
	}
	
	@Override
	public void stop() {
		container.stop();
	}
	
	String getJdbcUrl() {
		return "jdbc:mysql://" + container.getHost() + ":" + container.getFirstMappedPort() + "/" + DB_NAME;
	}
	
}