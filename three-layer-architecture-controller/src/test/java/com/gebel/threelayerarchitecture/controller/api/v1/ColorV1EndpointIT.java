package com.gebel.threelayerarchitecture.controller.api.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.gebel.threelayerarchitecture.controller._test.TestContainersManager;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.ColorV1Endpoint;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ColorV1EndpointIT {
	
	private static final TestContainersManager TEST_CONTAINERS_MANAGER = new TestContainersManager(); // Shared between all methods.
	private static final String API_URL_PATTERN = "http://localhost:%d/api/v1/colors";
	
	@LocalServerPort
	private int serverPort;
	
	@SpyBean
	private ColorV1Endpoint colorV1Endpoint;
	
	@DynamicPropertySource
	private static void setupContainersDynamicConfigurationProperties(DynamicPropertyRegistry registry) throws IOException {
		TEST_CONTAINERS_MANAGER.startContainers();
		TEST_CONTAINERS_MANAGER.setDynamicContainersConfiguration(registry);
	}
	
	@AfterAll
	private static void clearAll() {
		TEST_CONTAINERS_MANAGER.stopContainers();
	}
	
	@AfterEach
	void clear() throws Exception {
		TEST_CONTAINERS_MANAGER.getTestContainers().getMysqlDatabaseTestContainer().executeSqlScript("api-v1-color/deleteAllColors.sql");
	}
	
	@Test
	@Sql("classpath:api-v1-color/getFindAll_severalColors.sql")
	void givenSeveralColors_whenGetFindAll_thenAllColorsRetrieved() {
		// Given + sql
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ColorDto[]> response = restTemplate.getForEntity(serverPortUrl, ColorDto[].class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		ColorDto[] colors = response.getBody();
		assertEquals(3, colors.length);

		ColorDto color1 = colors[0];
		assertEquals("id_1", color1.getId());
		assertEquals("#000000", color1.getHexaCode());

		ColorDto color2 = colors[1];
		assertEquals("id_2", color2.getId());
		assertEquals("#000001", color2.getHexaCode());

		ColorDto color3 = colors[2];
		assertEquals("id_3", color3.getId());
		assertEquals("#000002", color3.getHexaCode());
	}
	
	@Test
	void givenInternalError_whenGetFindAll_thenGenericError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		when(colorV1Endpoint.getAllAvailableColors())
			.thenThrow(new IllegalArgumentException("Test"));
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiTechnicalErrorDto> response = restTemplate.getForEntity(serverPortUrl, ApiTechnicalErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
	@Test
	void givenValidColor_whenCreate_thenColorCreated() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		String hexaCodeToCreate = "#ABCDEF";
		HttpEntity<String> request = new HttpEntity<String>(hexaCodeToCreate, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ColorDto> response = restTemplate.postForEntity(serverPortUrl, request, ColorDto.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		ColorDto createdColor = response.getBody();
		assertNotNull(createdColor.getId());
		assertEquals("#ABCDEF", createdColor.getHexaCode());
	}
	
//	create 409 1
//	create 409 2
//	create 500
//	
//	delete 200
//	delete 500
	// TODO

}