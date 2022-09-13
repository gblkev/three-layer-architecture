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
public class RedisTestContainer extends GenericTestContainer {

	private static final int RANDOM_PORT = -1;
	private static final int CONTAINER_MAPPED_PORT = 6379;
	
	private final String dockerImage;
	private final int redisPort;
	
	private GenericContainer<?> container;
	
	public RedisTestContainer(String dockerImage) {
		super(true);
		this.dockerImage = dockerImage;
		this.redisPort = RANDOM_PORT;
		buildContainer();
	}
	
	public RedisTestContainer(String dockerImage, int redisPort) {
		super(false);
		this.dockerImage = dockerImage;
		this.redisPort = redisPort;
		buildContainer();
	}

	@SuppressWarnings("resource") // Resource closed by "stop()"
	private void buildContainer() {
		container = new GenericContainer<>(DockerImageName.parse(dockerImage))
			.withExposedPorts(CONTAINER_MAPPED_PORT)
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.redis")));
		if (!isUseRandomPort()) {
			container.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
				new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(redisPort), new ExposedPort(CONTAINER_MAPPED_PORT)))));
		}
	}

	@Override
	public void start() {
		LOGGER.info("Starting Redis...");
		container.start();
	}
	
	@Override
	public void stop() {
		container.stop();
	}
	
	@Override
	public void resetContainerData() throws Exception {
		LOGGER.info("Resetting Redis data...");
		// TODO clear redis caches
	}
	
	public String getHost() {
		return container.getHost();
	}
	
	public int getPort() {
		return container.getFirstMappedPort();
	}

}