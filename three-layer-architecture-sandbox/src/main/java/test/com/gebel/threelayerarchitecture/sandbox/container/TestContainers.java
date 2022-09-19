package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestContainers {

	@Getter
	private MysqlTestContainer mysqlTestContainer;
	
	@Getter
	private RedisTestContainer redisTestContainer;
	
	@Getter
	private ZookeeperKafkaTestContainers zookeeperKafkaTestContainers;
	
	@Getter
	private RestServicesTestContainer restServicesTestContainer;
	
	private final List<GenericTestContainer<?>> containers = new ArrayList<>();
	
	public void initMysqlContainerWithRandomPort(String mysqlDockerImage, String dbName, String mysqlUser, String mysqlPassword) {
		this.mysqlTestContainer = new MysqlTestContainer(mysqlDockerImage, dbName, mysqlUser, mysqlPassword);
		containers.add(mysqlTestContainer);
	}
	
	public void initMysqlContainerWithFixedPort(String mysqlDockerImage, String dbName, int mysqlPort, String mysqlUser, String mysqlPassword) {
		this.mysqlTestContainer = new MysqlTestContainer(mysqlDockerImage, dbName, mysqlPort, mysqlUser, mysqlPassword);
		containers.add(mysqlTestContainer);
	}
	
	public void initRedisContainerWithRandomPort(String redisDockerImage, String redisPassword, int redisDatabase, boolean loadInitScript) throws Exception {
		this.redisTestContainer = new RedisTestContainer(redisDockerImage, redisPassword, redisDatabase, loadInitScript);
		containers.add(redisTestContainer);
	}
	
	public void initRedisContainerWithFixedPort(String redisDockerImage, int redisPort, String redisPassword, int redisDatabase, boolean loadInitScript) throws Exception {
		this.redisTestContainer = new RedisTestContainer(redisDockerImage, redisPort, redisPassword, redisDatabase, loadInitScript);
		containers.add(redisTestContainer);
	}
	
	public void initZookeeperKafkaContainersWithRandomPort(String kafkaDockerImage, String topics) {
		this.zookeeperKafkaTestContainers = new ZookeeperKafkaTestContainers(kafkaDockerImage, topics);
		containers.add(zookeeperKafkaTestContainers);
	}
	
	public void initZookeeperKafkaContainersWithFixedPort(String kafkaDockerImage, int kafkaPort, int zookeeperPort, String kafkaTopics) {
		this.zookeeperKafkaTestContainers = new ZookeeperKafkaTestContainers(kafkaDockerImage, kafkaPort, zookeeperPort, kafkaTopics);
		containers.add(zookeeperKafkaTestContainers);
	}
	
	public void initRestServicesContainerWithRandomPort(String mockServerDockerImage, boolean initMocks) {
		this.restServicesTestContainer = new RestServicesTestContainer(mockServerDockerImage, initMocks);
		containers.add(restServicesTestContainer);
	}
	
	public void initRestServicesContainerWithFixedPort(String mockServerDockerImage, int mockServerPort, boolean initMocks) {
		this.restServicesTestContainer = new RestServicesTestContainer(mockServerDockerImage, mockServerPort, initMocks);
		containers.add(restServicesTestContainer);
	}
	
	public void startContainers() {
		LOGGER.info("Starting test containers...");
		Instant start = Instant.now();
		containers.stream()
			.parallel()
			.forEach(GenericTestContainer::start);
		Instant end = Instant.now();
		LOGGER.info("Test containers started in {}", Duration.between(start, end));
	}
	
	public void stopContainers() {
		LOGGER.info("Stopping test containers...");
		containers.stream()
			.parallel()
			.forEach(GenericTestContainer::stop);
		containers.clear();
	}
	
	public void resetContainersData() {
		LOGGER.info("Resetting test containers...");
		containers.stream()
			.parallel()
			.forEach(this::silentResetContainerData);
	}
	
	private void silentResetContainerData(GenericTestContainer<?> genericTestContainer) {
		try {
			genericTestContainer.resetContainerData();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}