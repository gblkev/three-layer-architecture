package test.com.gebel.threelayerarchitecture.sandbox.container;

import org.testcontainers.containers.MySQLContainer;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MysqlDatabaseTestContainer extends GenericTestContainer {

	private static final int RANDOM_PORT = -1;
	private static final int CONTAINER_MAPPED_PORT = 3306;
	private static final String INIT_SCRIPT_PATH = "create-mysql-database.sql";
	
	private final String mysqlVersion;
	private final String dbName;
	private final int dbPort;
	private final String dbUser;
	private final String dbPassword;
	
	private MySQLContainer<?> container;
	
	public MysqlDatabaseTestContainer(String mysqlVersion, String dbName, String dbUser, String dbPassword) {
		super(true);
		this.mysqlVersion = mysqlVersion;
		this.dbName = dbName;
		this.dbPort = RANDOM_PORT;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}
	
	public MysqlDatabaseTestContainer(String mysqlVersion, String dbName, int dbPort, String dbUser, String dbPassword) {
		super(false);
		this.mysqlVersion = mysqlVersion;
		this.dbName = dbName;
		this.dbPort = dbPort;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	@Override
	@SuppressWarnings("resource") // Resource closed by "stop()"
	public void start() {
		container = (MySQLContainer<?>) new MySQLContainer<>("mysql:" + mysqlVersion)
			.withDatabaseName(dbName)
			.withUsername(dbUser)
			.withPassword(dbPassword)
			.withExposedPorts(CONTAINER_MAPPED_PORT)
			.withInitScript(INIT_SCRIPT_PATH);
		if (!isUseRandomPort()) {
			container = container.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
				new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(dbPort), new ExposedPort(CONTAINER_MAPPED_PORT)))));
		}
		LOGGER.info("Starting MySQL database...");
		container.start();
	}
	
	@Override
	public void stop() {
		container.stop();
	}
	
	public String getJdbcUrl() {
		return "jdbc:mysql://" + container.getHost() + ":" + container.getFirstMappedPort() + "/" + container.getDatabaseName();
	}

}