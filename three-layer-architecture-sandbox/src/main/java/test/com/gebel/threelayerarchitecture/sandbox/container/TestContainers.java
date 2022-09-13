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
	
	private List<GenericTestContainer> containers = new ArrayList<>();
	
	public void initMysqlContainerWithRandomPort(String dockerImage, String dbName, String dbUser, String dbPassword) {
		this.mysqlTestContainer = new MysqlTestContainer(dockerImage, dbName, dbUser, dbPassword);
		containers.add(mysqlTestContainer);
	}
	
	public void initMysqlContainerWithFixedPort(String dockerImage, String dbName, int dbPort, String dbUser, String dbPassword) {
		this.mysqlTestContainer = new MysqlTestContainer(dockerImage, dbName, dbPort, dbUser, dbPassword);
		containers.add(mysqlTestContainer);
	}
	
	public void initRedisContainerWithRandomPort(String dockerImage) {
		this.redisTestContainer = new RedisTestContainer(dockerImage);
		containers.add(redisTestContainer);
	}
	
	public void initRedisContainerWithFixedPort(String dockerImage, int redisPort) {
		this.redisTestContainer = new RedisTestContainer(dockerImage, redisPort);
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
	
	private void silentResetContainerData(GenericTestContainer genericTestContainer) {
		try {
			genericTestContainer.resetContainerData();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}