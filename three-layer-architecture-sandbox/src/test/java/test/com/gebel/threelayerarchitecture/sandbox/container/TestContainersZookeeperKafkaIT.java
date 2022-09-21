package test.com.gebel.threelayerarchitecture.sandbox.container;

import static org.awaitility.Awaitility.waitAtMost;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
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
		String messageId = "message-id";
		String message = "This is a test message!";
		
		// When
		addMessageToKafka(messageId, message);
		
		// Then
		Map<String, String> messages = getKafkaMessages();
		assertEquals(1, messages.size());
		assertEquals("This is a test message!", messages.get("message-id"));
	}
	
	@SneakyThrows
	private void addMessageToKafka(String messageId, String message) {
		String bootstrapServers = getTestContainersManager().getTestContainers().getZookeeperKafkaTestContainers().getContainer().getBootstrapServers();
		
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, messageId, message);
		
		try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties)) {
			kafkaProducer.send(producerRecord).get();
		}
	}
	
	private Map<String, String> getKafkaMessages() {
		String bootstrapServers = getTestContainersManager().getTestContainers().getZookeeperKafkaTestContainers().getContainer().getBootstrapServers();

		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		
		try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
			kafkaConsumer.subscribe(List.of(TOPIC));
			Map<String, String> messages = new HashMap<>();
			waitAtMost(Duration.ofSeconds(2))
				.alias("Find messages in Kafka")
				.until(() -> {
					messages.putAll(pollMessages(kafkaConsumer));
					return !messages.isEmpty();
				});
			return messages;
		}
	}
	
	private Map<String, String> pollMessages(KafkaConsumer<String, String> kafkaConsumer) {
		ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
		Map<String, String> messages = new HashMap<>();
		records.forEach(record -> messages.put(record.key(), record.value()));
		return messages;
	}
	
}