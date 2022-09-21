package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.RecordsToDelete;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperKafkaTestContainers extends GenericTestContainer<KafkaContainer> {

	private static final int ZOOKEEPER_CONTAINER_MAPPED_PORT = KafkaContainer.ZOOKEEPER_PORT;
	private static final int KAFKA_CONTAINER_MAPPED_PORT = KafkaContainer.KAFKA_PORT;
	
	private final List<String> kafkaTopics;
	
	public ZookeeperKafkaTestContainers(String kafkaDockerImage, List<String> kafkaTopics) {
		this(kafkaDockerImage, RANDOM_PORT, RANDOM_PORT, kafkaTopics);
	}
	
	public ZookeeperKafkaTestContainers(String kafkaDockerImage, int kafkaPort, int zookeeperPort, List<String> kafkaTopics) {
		super("Zookeeper and Kafka");
		this.kafkaTopics = kafkaTopics;
		KafkaContainer kafkaContainer = buildContainers(kafkaDockerImage, kafkaPort, zookeeperPort, kafkaTopics);
		setContainer(kafkaContainer);
	}
	
	@SuppressWarnings("resource") // Resources closed by "stop()"
	private KafkaContainer buildContainers(String kafkaDockerImage, int kafkaPort, int zookeeperPort, List<String> kafkaTopics) {
		KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse(kafkaDockerImage))
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.kafka")));
		if (!isRandomPort(kafkaPort)) {
			kafkaContainer.getPortBindings().add(zookeeperPort + ":" + ZOOKEEPER_CONTAINER_MAPPED_PORT);
			kafkaContainer.getPortBindings().add(kafkaPort + ":" + KAFKA_CONTAINER_MAPPED_PORT);
		}
		return kafkaContainer;
	}
	
	@Override
	public void start() {
		super.start();
		createTopics();
	}
	
	@SneakyThrows
	private void createTopics() {
		try (AdminClient adminClient = buildKafkaAdminClient()) {
			List<NewTopic> topics = kafkaTopics.stream()
				.map(topic -> new NewTopic(topic, 1, (short) 1))
				.toList();
			adminClient.createTopics(topics)
				.all()
				.get(10, TimeUnit.SECONDS);
		}
	}
	
	private AdminClient buildKafkaAdminClient() {
		Properties properties = new Properties();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, getContainer().getBootstrapServers());
		return AdminClient.create(properties);
	}
	
	@Override
	public void resetContainerData() {
		LOGGER.info("Resetting Kafka data...");
		kafkaTopics.forEach(this::clearTopicData);
	}
	
	private void clearTopicData(String topic) {
		TopicPartition topicPartition = new TopicPartition(topic, 0);
		Map<TopicPartition, RecordsToDelete> deleteMap = new HashMap<>();
		deleteMap.put(topicPartition, RecordsToDelete.beforeOffset(Long.MAX_VALUE));
		try (AdminClient adminClient = buildKafkaAdminClient()) {
			adminClient.deleteRecords(deleteMap);
		}
	}
	
	@SneakyThrows
	public void publishMessage(String topic, Object message) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getContainer().getBootstrapServers());
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		
		ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, message);
		
		try (KafkaProducer<String, Object> kafkaProducer = new KafkaProducer<>(properties)) {
			kafkaProducer.send(producerRecord).get();
		}
	}

}