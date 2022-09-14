package test.com.gebel.threelayerarchitecture.sandbox.container;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperKafkaTestContainers extends GenericTestContainer<KafkaContainer> {

	private static final int ZOOKEEPER_CONTAINER_MAPPED_PORT = KafkaContainer.ZOOKEEPER_PORT;
	private static final int KAFKA_CONTAINER_MAPPED_PORT = KafkaContainer.KAFKA_PORT;
	
	public ZookeeperKafkaTestContainers(String kafkaDockerImage, String kafkaTopics) {
		this(kafkaDockerImage, RANDOM_PORT, RANDOM_PORT, kafkaTopics);
	}
	
	public ZookeeperKafkaTestContainers(String kafkaDockerImage, int kafkaPort, int zookeeperPort, String kafkaTopics) {
		super("Zookeeper and Kafka");
		KafkaContainer kafkaContainer = buildContainers(kafkaDockerImage, kafkaPort, zookeeperPort, kafkaTopics);
		setContainer(kafkaContainer);
	}
	
	@SuppressWarnings("resource") // Resources closed by "stop()"
	private KafkaContainer buildContainers(String kafkaDockerImage, int kafkaPort, int zookeeperPort, String kafkaTopics) {
		KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse(kafkaDockerImage))
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.kafka")))
			.withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true")
			.withEnv("KAFKA_CREATE_TOPICS", kafkaTopics);
		if (!isRandomPort(kafkaPort)) {
			kafkaContainer.getPortBindings().add(zookeeperPort + ":" + ZOOKEEPER_CONTAINER_MAPPED_PORT);
			kafkaContainer.getPortBindings().add(kafkaPort + ":" + KAFKA_CONTAINER_MAPPED_PORT);
		}
		return kafkaContainer;
	}
	
	@Override
	public void resetContainerData() throws Exception {
		LOGGER.info("Resetting Kafka data...");
		// TODO clear kafka content
	}

}