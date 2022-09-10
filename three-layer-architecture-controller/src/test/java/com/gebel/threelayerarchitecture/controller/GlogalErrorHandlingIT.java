package com.gebel.threelayerarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gebel.threelayerarchitecture.controller._test.TestContainersManager;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GlogalErrorHandlingIT {
	
	private static final TestContainersManager TEST_CONTAINERS_MANAGER = new TestContainersManager(); // Shared between all methods.
	
	@LocalServerPort
	private int serverPort;
	
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
	void givenInvalidUrl_whenCallingUrl_thenGenericNotFoundErrorMessage() throws Exception {
		// Given
		String invalidUrl = "http://localhost:%d/doesnotexist";
		String managementPortUrl = String.format(invalidUrl, serverPort);
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(managementPortUrl, String.class);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedJson = "{\"message\":\"Page not found\"}";
		assertEquals(mapper.readTree(expectedJson), mapper.readTree(response.getBody()));
	}

}