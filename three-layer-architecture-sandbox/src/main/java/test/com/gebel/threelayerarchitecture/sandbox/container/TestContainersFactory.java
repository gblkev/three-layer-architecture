package test.com.gebel.threelayerarchitecture.sandbox.container;

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
	
	@Value("${sandbox.kafka.docker-image}")
	private String kafkaDockerImage;
	@Value("${sandbox.kafka.port}")
	private int kafkaPort;
	@Value("${sandbox.zookeeper.port}")
	private int zookeeperPort;
	@Value("${sandbox.kafka.topics}")
	private String kafkaTopics;
	
	public TestContainers build() {
		if (useRandomPorts) {
			return buildContainersWithRandomPorts();
		}
		else {
			return buildContainersWithFixedPorts();
		}
	}
	
	private TestContainers buildContainersWithRandomPorts() {
		TestContainers testContainers = new TestContainers();
		testContainers.initMysqlContainerWithRandomPort(mysqlDockerImage, mysqlDbName, mysqlUser, mysqlPassword);
		testContainers.initRedisContainerWithRandomPort(redisDockerImage, redisPassword);
		testContainers.initZookeeperKafkaContainersWithRandomPort(kafkaDockerImage, kafkaTopics);
		return testContainers;
	}
	
	private TestContainers buildContainersWithFixedPorts() {
		TestContainers testContainers = new TestContainers();
		testContainers.initMysqlContainerWithFixedPort(mysqlDockerImage, mysqlDbName, mysqlPort, mysqlUser, mysqlPassword);
		testContainers.initRedisContainerWithFixedPort(redisDockerImage, redisPort, redisPassword);
		testContainers.initZookeeperKafkaContainersWithFixedPort(kafkaDockerImage, kafkaPort, zookeeperPort, kafkaTopics);
		return testContainers;
	}

}