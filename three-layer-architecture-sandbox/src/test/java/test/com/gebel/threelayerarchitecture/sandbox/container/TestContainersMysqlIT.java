package test.com.gebel.threelayerarchitecture.sandbox.container;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

import test.com.gebel.threelayerarchitecture.sandbox.container._test.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class TestContainersMysqlIT extends AbstractIntegrationTest {
	
	private static final String JDBC_URL_PATTERN = "jdbc:mysql://%s:%d/cars_db";
	private static final String MYSQL_USER = "test_user";
	private static final String MYSQL_PASSWORD = "test_password";

	@Test
	void givenMysqlDatabaseCreated_whenCreatingCars_thenCarsRetrieved() throws Exception {
		// Given + database created
		Class.forName("com.mysql.cj.jdbc.Driver");
		String host = getTestContainersManager().getTestContainers().getMysqlTestContainer().getHost();
		int port = getTestContainersManager().getTestContainers().getMysqlTestContainer().getPort();
		String jdbcUrl = String.format(JDBC_URL_PATTERN, host, port);

		// When
		createColors(jdbcUrl, MYSQL_USER, MYSQL_PASSWORD);
		List<List<String>> createdColors = getAllColors(jdbcUrl, MYSQL_USER, MYSQL_PASSWORD);
		
		// Then
		List<List<String>> expectedColors = List.of(
			List.of("color_id1", "#000000"),
			List.of("color_id2", "#000001"));
		
		assertEquals(expectedColors, createdColors);
	}
	
	private void createColors(String jdbcUrl, String mysqlUser, String mysqlPassword) throws SQLException {
		try (Connection connection = DriverManager.getConnection(jdbcUrl, mysqlUser, mysqlPassword)) {
			Statement statement = connection.createStatement();
			statement.addBatch("INSERT INTO color (id, hexa_code) VALUES ('color_id1', '#000000');");
			statement.addBatch("INSERT INTO color (id, hexa_code) VALUES ('color_id2', '#000001');");
			statement.executeBatch();
		}
	}
	
	private List<List<String>> getAllColors(String jdbcUrl, String mysqlUser, String mysqlPassword) throws SQLException {
		try (Connection connection = DriverManager.getConnection(jdbcUrl, mysqlUser, mysqlPassword)) {
			String selectQuery = "SELECT id, hexa_code FROM color;";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(selectQuery);
			List<List<String>> results = new ArrayList<>();
			while (resultSet.next()) {
				List<String> color = new ArrayList<>();
				color.add(resultSet.getString("id"));
				color.add(resultSet.getString("hexa_code"));
				results.add(color);
			}
			return results;
		}
	}
	
}