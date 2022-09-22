package com.gebel.threelayerarchitecture.controller.jmx;

import static org.awaitility.Awaitility.waitAtMost;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jolokia.client.J4pClient;
import org.jolokia.client.request.J4pExecRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;

import lombok.SneakyThrows;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class KafkaConsumersJmxResourceIT extends AbstractIntegrationTest {
	
	private static final String JOLOKIA_URL_PATTERN = "http://localhost:%d/actuator/jolokia";
	private static final String COLORS_API_URL_PATTERN = "http://localhost:%d/api/v2/colors";

	@Test
	void givenValidHexaCode_whenPublishCreateColorMessageToKafka_thenColorCreated() {
		// Given
		String hexaCode = "#ABCDEF";
		
		// When
		callPublishCreateColorMessageToKafkaJmxOperation(hexaCode);
		
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
		assertEquals("#ABCDEF", color.getHexaCode());
	}
	
	@SneakyThrows
	private void callPublishCreateColorMessageToKafkaJmxOperation(String hexaCode) {
		String jolokiaUrl = String.format(JOLOKIA_URL_PATTERN, getManagementPort());
		J4pClient j4pClient = new J4pClient(jolokiaUrl);
		J4pExecRequest j4pExecRequest = new J4pExecRequest(
			"threelayerarchitecture:name=kafka.consumers",
			"publishCreateColorMessageToKafka",
			hexaCode);
		j4pClient.execute(j4pExecRequest);
	}
	
	private List<ColorDto> getAllColorsFromApi() {
		String colorsApiUrl = String.format(COLORS_API_URL_PATTERN, getServerPort());
		TestRestTemplate restTemplate = new TestRestTemplate();
		return Arrays.asList(restTemplate.getForEntity(colorsApiUrl, ColorDto[].class).getBody());
	}
	
}