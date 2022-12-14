package com.gebel.threelayerarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringActuatorIT extends AbstractIntegrationTest {
	
	private static final String ACTUATOR_HEALTH_URL_PATTERN = "http://localhost:%d/actuator/health";
	private static final String ACTUATOR_JOLOKIA_URL_PATTERN = "http://localhost:%d/actuator/jolokia";
	
	private final TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	void givenActuatorExposedOnManagementPort_whenCallingActuatorHealth_thenActuatorAvailableOnManagementPort() throws Exception {
		// Given
		String managementPortUrl = String.format(ACTUATOR_HEALTH_URL_PATTERN, getManagementPort());
		
		// When
		ResponseEntity<String> response = restTemplate.getForEntity(managementPortUrl, String.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedJson = "{\"status\":\"UP\"}";
		assertEquals(mapper.readTree(expectedJson), mapper.readTree(response.getBody()));
	}
	
	@Test
	void givenActuatorExposedOnManagementPort_whenCallingActuatorHealth_thenActuatorNotAvailableOnServerPort() throws Exception {
		// Given
		String serverPortUrl = String.format(ACTUATOR_HEALTH_URL_PATTERN, getServerPort());
		
		// When
		ResponseEntity<String> response = restTemplate.getForEntity(serverPortUrl, String.class);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedJson = "{\"message\":\"Page not found\"}";
		assertEquals(mapper.readTree(expectedJson), mapper.readTree(response.getBody()));
	}
	
	@Test
	void givenJolokiaExposedOnManagementPort_whenCallingJolokia_thenJolokiaAvailableOnManagementPort() throws Exception {
		// Given
		String managementPortUrl = String.format(ACTUATOR_JOLOKIA_URL_PATTERN, getManagementPort());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(managementPortUrl, String.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}