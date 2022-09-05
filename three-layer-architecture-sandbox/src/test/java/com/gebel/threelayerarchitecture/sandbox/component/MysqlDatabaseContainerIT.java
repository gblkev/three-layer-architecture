package com.gebel.threelayerarchitecture.sandbox.component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MysqlDatabaseContainerIT {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/cars_db";
	private static final String DB_USER = "test_user";
	private static final String DB_PASSWORD = "test_password";
	private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	
	@Test
	void givenDatabaseCreated_whenQueryingAllCars_thenNoCars() throws SQLException, ClassNotFoundException {
		// Given
		// Database created
		Class.forName(DB_DRIVER);
		
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
			Statement statement = connection.createStatement();
			// When
			ResultSet resultSet = statement.executeQuery("SELECT * FROM car");
			while (resultSet.next()) {
				// Then
				Assertions.fail("There shouldn't be any result");
			}
		}
	}

}