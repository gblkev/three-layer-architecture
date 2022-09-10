package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TestContainers {

	private MysqlDatabaseTestContainer mysqlDatabaseTestContainer;
	
	public void initMysqlDatabaseContainerWithRandomPort(String mysqlVersion, String dbName, String dbUser, String dbPassword) {
		MysqlDatabaseTestContainer mysqlDatabaseTestContainer = new MysqlDatabaseTestContainer(mysqlVersion, dbName, dbUser, dbPassword);
		this.mysqlDatabaseTestContainer = mysqlDatabaseTestContainer;
	}
	
	public void initMysqlDatabaseContainerWithFixedPort(String mysqlVersion, String dbName, int dbPort, String dbUser, String dbPassword) {
		MysqlDatabaseTestContainer mysqlDatabaseTestContainer = new MysqlDatabaseTestContainer(mysqlVersion, dbName, dbPort, dbUser, dbPassword);
		this.mysqlDatabaseTestContainer = mysqlDatabaseTestContainer;
	}
	
	public void startContainers() {
		LOGGER.info("Starting test containers in parallel...");
		Instant start = Instant.now();
		Stream.of(mysqlDatabaseTestContainer)
			.forEach(GenericTestContainer::start);
		Instant end = Instant.now();
		LOGGER.info("Containers test started in {}", Duration.between(start, end));
	}
	
	public void stopContainers() {
		LOGGER.info("Stopping test containers...");
		Stream.of(mysqlDatabaseTestContainer)
			.forEach(GenericTestContainer::stop);
	}
	
}