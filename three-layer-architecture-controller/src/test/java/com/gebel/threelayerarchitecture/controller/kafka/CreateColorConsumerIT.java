package com.gebel.threelayerarchitecture.controller.kafka;

import static org.awaitility.Awaitility.waitAtMost;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.kafka.model.CreateColorModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CreateColorConsumerIT extends AbstractIntegrationTest {

	private static final String COLORS_API_URL_PATTERN = "http://localhost:%d/api/v2/colors";
	
	@Test
	void givenValidHexaCode_whenPublishingKafkaColorCreationMessage_thenColorCreated() {
		// Given
		String topic = CreateColorConsumer.KAFKA_TOPIC;
		CreateColorModel createColorModel = new CreateColorModel("#FFFFFF");
		
		// When
		getTestContainers().getZookeeperKafkaTestContainers().publishMessage(topic, createColorModel);
		
		// Then
		List<ColorDto> colors = new ArrayList<>();
		waitAtMost(Duration.ofSeconds(5))
			.alias("Wait for Kafka message to be consumed")
			.until(() -> {
				colors.addAll(getAllColorsFromApi());
				return !colors.isEmpty();
			});
		assertEquals(1, colors.size());
		
		ColorDto color = colors.get(0);
		assertEquals("#FFFFFF", color.getHexaCode());
	}
	
	private List<ColorDto> getAllColorsFromApi() {
		String url = String.format(COLORS_API_URL_PATTERN, getServerPort());
		TestRestTemplate restTemplate = new TestRestTemplate();
		return Arrays.asList(restTemplate.getForEntity(url, ColorDto[].class).getBody());
	}
	
}