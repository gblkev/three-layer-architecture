package com.gebel.threelayerarchitecture.sandbox.component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class) // Ensure to create the datasource after db creation.
class MysqlDatabaseContainerIT {

	@Autowired
	private DataSource dataSource;

	@Test
	void givenDatabaseCreated_whenQueryingAllCars_thenNoCars() throws SQLException {
		// Given
		// Database created
		
		// When
		try (Connection connection = dataSource.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM car");
			while (resultSet.next()) {
				// Then
				Assertions.fail("There shouldn't be any result");
			}
		}
	}

}