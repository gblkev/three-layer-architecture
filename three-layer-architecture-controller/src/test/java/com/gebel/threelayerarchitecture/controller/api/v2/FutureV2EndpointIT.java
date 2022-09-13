package com.gebel.threelayerarchitecture.controller.api.v2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.FutureDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiTechnicalErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.FutureV2Endpoint;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FutureV2EndpointIT extends AbstractIntegrationTest {
	
	private static final String API_URL_PATTERN = "http://localhost:%d/api/v2/future";
	
	@LocalServerPort
	private int serverPort;
	
	@SpyBean
	private FutureV2Endpoint futureV2Endpoint;
	
	@Test
	void givenAvailableApi_whenReadFuture_thenDataRetrieved() {
		// Given + sql
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<FutureDto> response = restTemplate.getForEntity(serverPortUrl, FutureDto.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		FutureDto futureDto = response.getBody();
		assertEquals("This is a future feature that is not released yet", futureDto.getMessage());
	}
	
	@Test
	void givenInternalError_whenReadFuture_thenGenericError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		when(futureV2Endpoint.readFuture())
			.thenThrow(new IllegalArgumentException("Test"));
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiTechnicalErrorDto> response = restTemplate.getForEntity(serverPortUrl, ApiTechnicalErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
}