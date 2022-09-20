package test.com.gebel.threelayerarchitecture.sandbox.container;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

import lombok.SneakyThrows;
import test.com.gebel.threelayerarchitecture.sandbox.container._test.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class TestContainersZookeeperKafkaIT extends AbstractIntegrationTest {
	
	private static final String TOPIC = "threelayerarchitecture.create-color";
	private static final String GROUP_ID = "threelayerarchitecture";
	
	@Test
	void givenValidKafkaMessage_whenAddingMessageToKafka_thenMessageRetrieved() {
		// Given
		String message = "This is a test message!";
		
		// When
		addMessageToKafka(message);
		
		// Then
		List<String> messages = getKafkaMessages();
		assertEquals(1, messages.size());
		assertEquals("This is a test message!", messages.get(0));
	}
	
	@SneakyThrows
	private void addMessageToKafka(String message) {
		String host = getTestContainersManager().getTestContainers().getZookeeperKafkaTestContainers().getHost();
		int port = getTestContainersManager().getTestContainers().getZookeeperKafkaTestContainers().getPort();
		String bootstrapServers = host + ":" + port;
		
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(TOPIC, message);
		
		try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties)) {
			kafkaProducer.send(producerRecord).get();
		}
	}
	
	private List<String> getKafkaMessages() {
		String host = getTestContainersManager().getTestContainers().getZookeeperKafkaTestContainers().getHost();
		int port = getTestContainersManager().getTestContainers().getZookeeperKafkaTestContainers().getPort();
		String bootstrapServers = host + ":" + port;
		
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		
		try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			kafkaConsumer.subscribe(List.of(TOPIC));
			List<String> messages = new ArrayList<>();
			Awaitility.waitAtMost(Duration.ofSeconds(2))
				.alias("Find messages in Kafka")
				.until(() -> {
					List<String> currentMessages = pollMessages(kafkaConsumer);
					return !currentMessages.isEmpty();
				});
			return messages;
		}
	}
	
	private List<String> pollMessages(KafkaConsumer<String, String> kafkaConsumer) {
		ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
		List<String> messages = new ArrayList<>();
		records.forEach(record -> messages.add(record.value()));
		return messages;
	}
	
}