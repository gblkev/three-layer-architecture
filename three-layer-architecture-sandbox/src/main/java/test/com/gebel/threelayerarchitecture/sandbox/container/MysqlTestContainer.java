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
public class MysqlTestContainer extends GenericTestContainer<MySQLContainer<?>> {

	private static final int CONTAINER_MAPPED_PORT = MySQLContainer.MYSQL_PORT;
	private static final String INIT_SCRIPT_PATH = "mysql-create-objects.sql";
	private static final String RESET_SCRIPT_PATH = "mysql-reset.sql";
	
	public MysqlTestContainer(String mysqlDockerImage, String dbName, String mysqlUser, String mysqlPassword) {
		this(mysqlDockerImage, dbName, RANDOM_PORT, mysqlUser, mysqlPassword);
	}
	
	public MysqlTestContainer(String mysqlDockerImage, String dbName, int mysqlPort, String mysqlUser, String mysqlPassword) {
		super("MySQL");
		MySQLContainer<?> container = buildContainer(mysqlDockerImage, dbName, mysqlPort, mysqlUser, mysqlPassword);
		setContainer(container);
	}
	
	@SuppressWarnings("resource") // Resource closed by "stop()"
	private MySQLContainer<?> buildContainer(String mysqlDockerImage, String dbName, int mysqlPort, String mysqlUser, String mysqlPassword) {
		MySQLContainer<?> container = (MySQLContainer<?>) new MySQLContainer<>(DockerImageName.parse(mysqlDockerImage))
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.mysql")))
			.withDatabaseName(dbName)
			.withUsername(mysqlUser)
			.withPassword(mysqlPassword)
			.withInitScript(INIT_SCRIPT_PATH);
		if (!isRandomPort(mysqlPort)) {
			container.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
				new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(mysqlPort), new ExposedPort(CONTAINER_MAPPED_PORT)))));
		}
		return container;
	}

	@Override
	public void resetContainerData() throws Exception {
		LOGGER.info("Resetting MySQL data...");
		executeSqlScript(RESET_SCRIPT_PATH);
	}
	
	public void executeSqlScript(String scriptPath) throws Exception {
		JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(getContainer(), "");
		URL resource = MysqlTestContainer.class.getClassLoader().getResource(scriptPath);
		String scripts = IOUtils.toString(resource, StandardCharsets.UTF_8);
		ScriptUtils.executeDatabaseScript(jdbcDatabaseDelegate, scriptPath, scripts);
	}
	
	public String getJdbcUrl() {
		return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getContainer().getDatabaseName();
	}

}