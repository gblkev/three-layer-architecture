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

	private static final int MYSQL_PORT;
	private static final int REDIS_PORT;
	private static final int KAFKA_PORT;
	private static final int ZOOKEEPER_PORT;
	
	@Value("${sandbox.mysql.port}")
	private int mysqlPort;
	
	@Value("${sandbox.redis.port}")
	private int redisPort;
	
	@Value("${sandbox.kafka.port}")
	private int kafkaPort;
	
	@Value("${sandbox.zookeeper.port}")
	private int zookeeperPort;
	
	static {
		MYSQL_PORT = getFreeTcpPort();
		REDIS_PORT = getFreeTcpPort();
		KAFKA_PORT = getFreeTcpPort();
		ZOOKEEPER_PORT = getFreeTcpPort();
	}
	
	private static int getFreeTcpPort() {
		try (ServerSocket freeServerSocket = new ServerSocket(0)) {
			return freeServerSocket.getLocalPort();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@DynamicPropertySource
    static void mysqlDynamicPortProperties(DynamicPropertyRegistry registry) throws IOException {
        registry.add("sandbox.mysql.port", () -> String.valueOf(MYSQL_PORT));
        registry.add("sandbox.redis.port", () -> String.valueOf(REDIS_PORT));
        registry.add("sandbox.kafka.port", () -> String.valueOf(KAFKA_PORT));
        registry.add("sandbox.zookeeper.port", () -> String.valueOf(ZOOKEEPER_PORT));
    }
	
	@Test
	void givenDatabaseCreated_whenQueryingAllCars_thenNoCars() throws SQLException, ClassNotFoundException {
		// Given
		// Database created
		Class.forName("com.mysql.cj.jdbc.Driver");
		String dbUrl = "jdbc:mysql://localhost:" + mysqlPort + "/cars_db";

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
	
	// TODO add tests for redis and kafka (ssl and non ssl)
	
}