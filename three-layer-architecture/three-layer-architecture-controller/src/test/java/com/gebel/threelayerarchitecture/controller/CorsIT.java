package com.gebel.threelayerarchitecture.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.gebel.threelayerarchitecture.controller._test.TestContainersManager;

@SpringBootTest(
	webEnvironment = WebEnvironment.RANDOM_PORT,
	properties = {
		"web.cors.allowed-origins=http://allowed-origin.com",
		"web.cors.allowed-methods=GET"
	})
class CorsIT {
	
	private static final TestContainersManager TEST_CONTAINERS_MANAGER = new TestContainersManager(); // Shared between all methods.
	private static final String API_GET_URL_PATTERN = "http://localhost:%d/api/v1/colors";
	private static final String API_DELETE_URL_PATTERN = "http://localhost:%d/api/v1/colors/fakeColorId";
	
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
	void givenHostAllowed_whenCallingApi_thenApiReachable() throws Exception {
		// Given
		String apiUrl = String.format(API_GET_URL_PATTERN, serverPort);
		HttpRequest request = HttpRequest.newBuilder()
			.uri(new URI(apiUrl))
			.header("Origin", "http://allowed-origin.com")
			.header("Access-Control-Request-Method", "GET")
			.GET()
			.build();
		
		// When
		HttpResponse<String> response = HttpClient.newBuilder()
			.build()
			.send(request, BodyHandlers.ofString());
		
		// Then
		assertEquals(HttpStatus.OK.value(), response.statusCode());
	}
	
	@Test
	void givenHostNotAllowed_whenCallingApi_thenApiNotReachable() throws Exception {
		// Given
		String apiUrl = String.format(API_GET_URL_PATTERN, serverPort);
		HttpRequest request = HttpRequest.newBuilder()
			.uri(new URI(apiUrl))
			.header("Origin", "http://not-allowed-origin.com")
			.header("Access-Control-Request-Method", "GET")
			.GET()
			.build();
		
		// When
		HttpResponse<String> response = HttpClient.newBuilder()
			.build()
			.send(request, BodyHandlers.ofString());
		
		// Then
		assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
		assertEquals("Invalid CORS request", response.body());
	}
	
	@Test
	void givenMethodNotAllowed_whenCallingApi_thenApiNotReachable() throws Exception {
		// Given
		String apiUrl = String.format(API_DELETE_URL_PATTERN, serverPort);
		HttpRequest request = HttpRequest.newBuilder()
			.uri(new URI(apiUrl))
			.header("Origin", "http://allowed-origin.com")
			.header("Access-Control-Request-Method", "DELETE")
			.DELETE()
			.build();
		
		// When
		HttpResponse<String> response = HttpClient.newBuilder()
			.build()
			.send(request, BodyHandlers.ofString());
		
		// Then
		assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
		assertEquals("Invalid CORS request", response.body());
	}
	
}