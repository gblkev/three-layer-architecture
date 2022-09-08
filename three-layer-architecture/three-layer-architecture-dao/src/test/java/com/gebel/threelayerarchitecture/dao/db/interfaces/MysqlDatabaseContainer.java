package com.gebel.threelayerarchitecture.dao.db.interfaces;

import org.testcontainers.containers.MySQLContainer;

public class MysqlDatabaseContainer {

	private static final String MYSQL_VERSION = "mysql:8.0.11";
	private static final String DB_NAME = "cars_db";
	private static final String DB_USER = "test_user";
	private static final String DB_PASSWORD = "test_password";
	private static final String INIT_SCRIPT_PATH = "db/create-mysql-database.sql";
	
	private MySQLContainer<?> container;
	
	@SuppressWarnings("resource")
	public MysqlDatabaseContainer() {
		container = new MySQLContainer<>(MysqlDatabaseContainer.MYSQL_VERSION)
			.withDatabaseName(DB_NAME)
			.withUsername(DB_USER)
			.withPassword(DB_PASSWORD)
			.withInitScript(INIT_SCRIPT_PATH);
	}
	
	public void start() {
		container.start();
	}
	
	public void stop() {
		container.stop();
	}
	
	public String getJdbcUrl() {
		return "jdbc:mysql://" + container.getHost() + ":" + container.getFirstMappedPort() + "/" + DB_NAME;
	}
	
}