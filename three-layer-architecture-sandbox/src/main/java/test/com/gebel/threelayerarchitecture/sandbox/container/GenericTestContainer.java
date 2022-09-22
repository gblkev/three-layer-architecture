package test.com.gebel.threelayerarchitecture.sandbox.container;

import org.testcontainers.containers.GenericContainer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class GenericTestContainer<T extends GenericContainer<?>> {

	protected static final int RANDOM_PORT = -1;
	
	private final String containerNameInLogs;
	
	@Getter
	@Setter
	private T container;
	
	public void start() {
		LOGGER.info("Starting {}...", containerNameInLogs);
		container.start();
	}
	
	public void stop() {
		LOGGER.info("Stopping {}...", containerNameInLogs);
		container.stop();
	}
	
	public void pause() {
		LOGGER.info("Pausing {}...", containerNameInLogs);
		container.getDockerClient()
			.pauseContainerCmd(container.getContainerId())
			.exec();
	}
	
	public void unpause() {
		LOGGER.info("Unpausing {}...", containerNameInLogs);
		container.getDockerClient()
			.unpauseContainerCmd(container.getContainerId())
			.exec();
	}
	
	public String getHost() {
		return container.getHost();
	}
	
	public int getPort() {
		return container.getFirstMappedPort();
	}
	
	public boolean isRandomPort(int port) {
		return port == RANDOM_PORT;
	}
	
	public abstract void resetContainerData();
	
}