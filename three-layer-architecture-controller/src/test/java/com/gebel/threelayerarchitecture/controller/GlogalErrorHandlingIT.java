package com.gebel.threelayerarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GlogalErrorHandlingIT extends AbstractIntegrationTest {
	
	@LocalServerPort
	private int serverPort;
	
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