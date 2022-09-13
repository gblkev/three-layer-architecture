package test.com.gebel.threelayerarchitecture.sandbox.container;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperKafkaTestContainers extends GenericTestContainer {

	private static final int RANDOM_PORT = -1;
	private static final int ZOOKEEPER_CONTAINER_MAPPED_PORT = KafkaContainer.ZOOKEEPER_PORT;
	private static final int KAFKA_CONTAINER_MAPPED_PORT = KafkaContainer.KAFKA_PORT;
	
	private final String kafkaDockerImage;
	private final int kafkaPort;
	private final int zookeeperPort;
	private final String kafkaTopics;
	
	private KafkaContainer kafkaContainer;
	
	public ZookeeperKafkaTestContainers(String kafkaDockerImage, String kafkaTopics) {
		super(true);
		this.kafkaDockerImage = kafkaDockerImage;
		this.kafkaPort = RANDOM_PORT;
		this.zookeeperPort = RANDOM_PORT;
		this.kafkaTopics = kafkaTopics;
		buildContainers();
	}
	
	public ZookeeperKafkaTestContainers(String kafkaDockerImage, int kafkaPort, int zookeeperPort, String kafkaTopics) {
		super(false);
		this.kafkaDockerImage = kafkaDockerImage;
		this.kafkaPort = kafkaPort;
		this.zookeeperPort = zookeeperPort;
		this.kafkaTopics = kafkaTopics;
		buildContainers();
	}
	
	@SuppressWarnings("resource") // Resources closed by "stop()"
	private void buildContainers() {
		kafkaContainer = new KafkaContainer(DockerImageName.parse(kafkaDockerImage))
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.kafka")))
			.withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true")
			.withEnv("KAFKA_CREATE_TOPICS", kafkaTopics);
		if (!isUseRandomPort()) {
			kafkaContainer.getPortBindings().add(zookeeperPort + ":" + ZOOKEEPER_CONTAINER_MAPPED_PORT);
			kafkaContainer.getPortBindings().add(kafkaPort + ":" + KAFKA_CONTAINER_MAPPED_PORT);
		}
	}

	@Override
	public void start() {
		LOGGER.info("Starting Zookeeper and Kafka...");
		kafkaContainer.start();
	}
	
	@Override
	public void stop() {
		kafkaContainer.stop();
	}
	
	@Override
	public void resetContainerData() throws Exception {
		LOGGER.info("Resetting Kafka data...");
		// TODO clear kafka content
	}

}