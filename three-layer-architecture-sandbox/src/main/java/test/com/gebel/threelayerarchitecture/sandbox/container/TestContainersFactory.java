package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestContainersFactory {
	
	@Value("${sandbox.use-random-ports}")
	private boolean useRandomPorts;
	
	@Value("${sandbox.mysql.docker-image}")
	private String mysqlDockerImage;
	@Value("${sandbox.mysql.db-name}")
	private String mysqlDbName;
	@Value("${sandbox.mysql.port}")
	private int mysqlPort;
	@Value("${sandbox.mysql.user}")
	private String mysqlUser;
	@Value("${sandbox.mysql.password}")
	private String mysqlPassword;
	
	@Value("${sandbox.redis.docker-image}")
	private String redisDockerImage;
	@Value("${sandbox.redis.port}")
	private int redisPort;
	@Value("${sandbox.redis.password}")
	private String redisPassword;
	@Value("${sandbox.redis.database}")
	private int redisDatabase;
	@Value("${sandbox.redis.load-init-script}")
	private boolean redisLoadInitScript;
	
	@Value("${sandbox.rest.mockserver-docker-image}")
	private String restMockServerDockerImage;
	@Value("${sandbox.rest.mockserver-port}")
	private int restMockServerPort;
	@Value("${sandbox.rest.init-mocks}")
	private boolean restInitMocks;
	
	@Value("${sandbox.kafka.docker-image}")
	private String kafkaDockerImage;
	@Value("${sandbox.kafka.port}")
	private int kafkaPort;
	@Value("${sandbox.zookeeper.port}")
	private int zookeeperPort;
	@Value("${sandbox.kafka.topics}")
	private List<String> kafkaTopics;
	
	public TestContainers build() throws Exception {
		if (useRandomPorts) {
			return buildContainersWithRandomPorts();
		}
		else {
			return buildContainersWithFixedPorts();
		}
	}
	
	private TestContainers buildContainersWithRandomPorts() throws Exception {
		TestContainers testContainers = new TestContainers();
		testContainers.initMysqlContainerWithRandomPort(mysqlDockerImage, mysqlDbName, mysqlUser, mysqlPassword);
		testContainers.initRedisContainerWithRandomPort(redisDockerImage, redisPassword, redisDatabase, redisLoadInitScript);
		testContainers.initZookeeperKafkaContainersWithRandomPort(kafkaDockerImage, kafkaTopics);
		testContainers.initRestServicesContainerWithRandomPort(restMockServerDockerImage, restInitMocks);
		return testContainers;
	}
	
	private TestContainers buildContainersWithFixedPorts() throws Exception {
		TestContainers testContainers = new TestContainers();
		testContainers.initMysqlContainerWithFixedPort(mysqlDockerImage, mysqlDbName, mysqlPort, mysqlUser, mysqlPassword);
		testContainers.initRedisContainerWithFixedPort(redisDockerImage, redisPort, redisPassword, redisDatabase, redisLoadInitScript);
		testContainers.initZookeeperKafkaContainersWithFixedPort(kafkaDockerImage, kafkaPort, zookeeperPort, kafkaTopics);
		testContainers.initRestServicesContainerWithFixedPort(restMockServerDockerImage, restMockServerPort, restInitMocks);
		return testContainers;
	}

}