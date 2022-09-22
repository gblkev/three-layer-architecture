package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestContainers {

	private static final String MYSQL_DOCKER_IMAGE = "mysql:8.0.11";
	private static final String REDIS_DOCKER_IMAGE = "redis:7.0.4";
	private static final String KAFKA_DOCKER_IMAGE = "confluentinc/cp-kafka:7.2.1";
	private static final String REST_SERVICES_MOCKSERVER_DOCKER_IMAGE = "jamesdbloom/mockserver:mockserver-5.13.2";
	
	@Getter
	private MysqlTestContainer mysqlTestContainer;
	
	@Getter
	private RedisTestContainer redisTestContainer;
	
	@Getter
	private ZookeeperKafkaTestContainers zookeeperKafkaTestContainers;
	
	@Getter
	private RestServicesTestContainer restServicesTestContainer;
	
	private final List<GenericTestContainer<?>> containers = new ArrayList<>();
	
	public void initMysqlContainerWithRandomPort(String dbName, String mysqlUser, String mysqlPassword) {
		this.mysqlTestContainer = new MysqlTestContainer(MYSQL_DOCKER_IMAGE, dbName, mysqlUser, mysqlPassword);
		containers.add(mysqlTestContainer);
	}
	
	public void initMysqlContainerWithFixedPort(String dbName, int mysqlPort, String mysqlUser, String mysqlPassword) {
		this.mysqlTestContainer = new MysqlTestContainer(MYSQL_DOCKER_IMAGE, dbName, mysqlPort, mysqlUser, mysqlPassword);
		containers.add(mysqlTestContainer);
	}
	
	public void initRedisContainerWithRandomPort(String redisPassword, int redisDatabase, boolean loadInitScript) throws Exception {
		this.redisTestContainer = new RedisTestContainer(REDIS_DOCKER_IMAGE, redisPassword, redisDatabase, loadInitScript);
		containers.add(redisTestContainer);
	}
	
	public void initRedisContainerWithFixedPort(int redisPort, String redisPassword, int redisDatabase, boolean loadInitScript) throws Exception {
		this.redisTestContainer = new RedisTestContainer(REDIS_DOCKER_IMAGE, redisPort, redisPassword, redisDatabase, loadInitScript);
		containers.add(redisTestContainer);
	}
	
	public void initZookeeperKafkaContainersWithRandomPort(List<String> topics) {
		this.zookeeperKafkaTestContainers = new ZookeeperKafkaTestContainers(KAFKA_DOCKER_IMAGE, topics);
		containers.add(zookeeperKafkaTestContainers);
	}
	
	public void initZookeeperKafkaContainersWithFixedPort(int kafkaPort, int zookeeperPort, List<String> kafkaTopics) {
		this.zookeeperKafkaTestContainers = new ZookeeperKafkaTestContainers(KAFKA_DOCKER_IMAGE, kafkaPort, zookeeperPort, kafkaTopics);
		containers.add(zookeeperKafkaTestContainers);
	}
	
	public void initRestServicesContainerWithRandomPort(boolean initMocks) {
		this.restServicesTestContainer = new RestServicesTestContainer(REST_SERVICES_MOCKSERVER_DOCKER_IMAGE, initMocks);
		containers.add(restServicesTestContainer);
	}
	
	public void initRestServicesContainerWithFixedPort(int mockServerPort, boolean initMocks) {
		this.restServicesTestContainer = new RestServicesTestContainer(REST_SERVICES_MOCKSERVER_DOCKER_IMAGE, mockServerPort, initMocks);
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