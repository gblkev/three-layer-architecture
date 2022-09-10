package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class TestContainersIT {

	private static final int FREE_TCP_PORT;
	
	@Value("${sandbox.db.port}")
	private int dbPort;
	
	static {
		try (ServerSocket freeServerSocket = new ServerSocket(0)) {
			FREE_TCP_PORT = freeServerSocket.getLocalPort();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@DynamicPropertySource
    static void mysqlDynamicPortProperties(DynamicPropertyRegistry registry) throws IOException {
        registry.add("sandbox.db.port", () -> String.valueOf(FREE_TCP_PORT));
    }
	
	@Test
	void givenDatabaseCreated_whenQueryingAllCars_thenNoCars() throws SQLException, ClassNotFoundException {
		// Given
		// Database created
		Class.forName("com.mysql.cj.jdbc.Driver");
		String dbUrl = "jdbc:mysql://localhost:" + dbPort + "/cars_db";

		try (Connection connection = DriverManager.getConnection(dbUrl, "test_user", "test_password")) {
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