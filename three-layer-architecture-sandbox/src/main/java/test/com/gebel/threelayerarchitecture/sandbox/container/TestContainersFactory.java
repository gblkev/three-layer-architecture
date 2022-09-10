package test.com.gebel.threelayerarchitecture.sandbox.container;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestContainersFactory {
	
	@Value("${sandbox.use-random-ports}")
	private boolean useRandomPorts;
	
	@Value("${sandbox.db.mysql-version}")
	private String mysqlVersion;
	@Value("${sandbox.db.name}")
	private String dbName;
	@Value("${sandbox.db.port}")
	private int dbPort;
	@Value("${sandbox.db.user}")
	private String dbUser;
	@Value("${sandbox.db.password}")
	private String dbPassword;
	
	public TestContainers build() {
		TestContainers testContainers = new TestContainers();
		if (useRandomPorts) {
			testContainers.initMysqlDatabaseContainerWithRandomPort(mysqlVersion, dbName, dbUser, dbPassword);
		}
		else {
			testContainers.initMysqlDatabaseContainerWithFixedPort(mysqlVersion, dbName, dbPort, dbUser, dbPassword);
		}
		return testContainers;
	}

}