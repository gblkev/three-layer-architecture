package test.com.gebel.threelayerarchitecture.sandbox.container;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisTestContainer extends GenericTestContainer<GenericContainer<?>> {

	private static final int CONTAINER_MAPPED_PORT = 6379;

	public RedisTestContainer(String redisDockerImage, String redisPassword) {
		this(redisDockerImage, RANDOM_PORT, redisPassword);
	}
	
	public RedisTestContainer(String redisDockerImage, int redisPort, String redisPassword) {
		super("Redis");
		GenericContainer<?> container = buildContainer(redisDockerImage, redisPort, redisPassword);
		setContainer(container);
	}

	@SuppressWarnings("resource") // Resource closed by "stop()"
	private GenericContainer<?> buildContainer(String redisDockerImage, int redisPort, String redisPassword) {
		GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse(redisDockerImage))
			.withExposedPorts(CONTAINER_MAPPED_PORT)
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.redis")))
			.withCommand("redis-server", "--requirepass", redisPassword);
		if (!isRandomPort(redisPort)) {
			container.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
				new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(redisPort), new ExposedPort(CONTAINER_MAPPED_PORT)))));
		}
		return container;
	}
	
	@Override
	public void resetContainerData() throws Exception {
		LOGGER.info("Resetting Redis data...");
		// TODO clear redis caches
	}
	
}