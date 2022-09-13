package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import org.testcontainers.utility.DockerImageName;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MysqlTestContainer extends GenericTestContainer {

	private static final int RANDOM_PORT = -1;
	private static final int CONTAINER_MAPPED_PORT = MySQLContainer.MYSQL_PORT;
	private static final String INIT_SCRIPT_PATH = "mysql-init.sql";
	private static final String RESET_SCRIPT_PATH = "mysql-reset.sql";
	
	private final String dockerImage;
	private final String dbName;
	private final int dbPort;
	private final String dbUser;
	private final String dbPassword;
	
	private MySQLContainer<?> container;
	
	public MysqlTestContainer(String dockerImage, String dbName, String dbUser, String dbPassword) {
		super(true);
		this.dockerImage = dockerImage;
		this.dbName = dbName;
		this.dbPort = RANDOM_PORT;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		buildContainer();
	}
	
	public MysqlTestContainer(String dockerImage, String dbName, int dbPort, String dbUser, String dbPassword) {
		super(false);
		this.dockerImage = dockerImage;
		this.dbName = dbName;
		this.dbPort = dbPort;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		buildContainer();
	}
	
	@SuppressWarnings("resource") // Resource closed by "stop()"
	private void buildContainer() {
		container = (MySQLContainer<?>) new MySQLContainer<>(DockerImageName.parse(dockerImage))
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.mysql")))
			.withDatabaseName(dbName)
			.withUsername(dbUser)
			.withPassword(dbPassword)
			.withInitScript(INIT_SCRIPT_PATH);
		if (!isUseRandomPort()) {
			container.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
				new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(dbPort), new ExposedPort(CONTAINER_MAPPED_PORT)))));
		}
	}

	@Override
	public void start() {
		LOGGER.info("Starting MySQL...");
		container.start();
	}
	
	@Override
	public void stop() {
		container.stop();
	}
	
	@Override
	public void resetContainerData() throws Exception {
		LOGGER.info("Resetting MySQL data...");
		executeSqlScript(RESET_SCRIPT_PATH);
	}
	
	public void executeSqlScript(String scriptPath) throws Exception {
		JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
		URL resource = MysqlTestContainer.class.getClassLoader().getResource(scriptPath);
		String scripts = IOUtils.toString(resource, StandardCharsets.UTF_8);
		ScriptUtils.executeDatabaseScript(jdbcDatabaseDelegate, scriptPath, scripts);
	}
	
	public String getJdbcUrl() {
		return "jdbc:mysql://" + container.getHost() + ":" + container.getFirstMappedPort() + "/" + container.getDatabaseName();
	}

}