package com.gebel.threelayerarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gebel.threelayerarchitecture.controller._test.TestContainersManager;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringActuatorIT {
	
	private static final TestContainersManager TEST_CONTAINERS_MANAGER = new TestContainersManager(); // Shared between all methods.
	private static final String ACTUATOR_URL_PATTERN = "http://localhost:%d/actuator/health";
	
	@LocalServerPort
	private int serverPort;
	
	@LocalManagementPort
	private int managementPort;
	
	@DynamicPropertySource
	private static void setupContainersDynamicConfigurationProperties(DynamicPropertyRegistry registry) throws IOException {
		TEST_CONTAINERS_MANAGER.startContainers();
		TEST_CONTAINERS_MANAGER.setDynamicContainersConfiguration(registry);
	}
	
	@AfterAll
	private static void clearAll() {
		TEST_CONTAINERS_MANAGER.stopContainers();
	}
	
	@Test
	void givenActuatorExposedOnManagementPort_whenCallingActuator_thenActuatorAvailableOnManagementPort() throws Exception {
		// Given
		String managementPortUrl = String.format(ACTUATOR_URL_PATTERN, managementPort);
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(managementPortUrl, String.class);
		
		// Then
		ObjectMapper mapper = new ObjectMapper();
		String expectedJson = "{\"status\":\"UP\"}";
		assertEquals(mapper.readTree(expectedJson), mapper.readTree(response.getBody()));
	}
	
	@Test
	void givenActuatorExposedOnManagementPort_whenCallingActuator_thenActuatorNotAvailableOnServerPort() throws Exception {
		// Given
		String serverPortUrl = String.format(ACTUATOR_URL_PATTERN, serverPort);
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(serverPortUrl, String.class);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedJson = "{\"message\":\"Page not found\"}";
		assertEquals(mapper.readTree(expectedJson), mapper.readTree(response.getBody()));
	}

}