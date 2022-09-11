package com.gebel.threelayerarchitecture.controller.api.v2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.gebel.threelayerarchitecture.controller._test.TestContainersManager;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorCodeDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiTechnicalErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.ColorV2Endpoint;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ColorV2EndpointIT {
	
	private static final TestContainersManager TEST_CONTAINERS_MANAGER = new TestContainersManager(); // Shared between all methods.
	private static final String API_URL_PATTERN = "http://localhost:%d/api/v2/colors";
	private static final String DELETE_BY_ID_API_URL_PATTERN = API_URL_PATTERN + "/{colorId}";
	
	@LocalServerPort
	private int serverPort;
	
	@SpyBean
	private ColorV2Endpoint colorV2Endpoint;
	
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
		TEST_CONTAINERS_MANAGER.getTestContainers().getMysqlDatabaseTestContainer().executeSqlScript("api-v2/color/deleteAllColors.sql");
	}
	
	@Test
	@Sql("classpath:api-v2/color/get_findAll_createSeveralColors.sql")
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
		when(colorV2Endpoint.getAllAvailableColors())
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
	void givenValidColor_whenPostCreate_thenColorCreated() {
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
	
	@Test
	void givenInvalidColor_whenPostCreate_thenInvalidHexaCodeError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		String hexaCodeToCreate = "#ZZZZZZ";
		HttpEntity<String> request = new HttpEntity<String>(hexaCodeToCreate, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(serverPortUrl, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.COLOR_INVALID_HEXA_CODE, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.COLOR_INVALID_HEXA_CODE.getDescription(), error.getMessage());
	}
	
	@Test
	@Sql("classpath:api-v2/color/post_create_createColor.sql")
	void givenColorAlreadyExists_whenPostCreate_thenColorAlreadyExistsError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		String hexaCodeToCreate = "#000000";
		HttpEntity<String> request = new HttpEntity<String>(hexaCodeToCreate, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(serverPortUrl, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.COLOR_SAME_HEXA_CODE_ALREADY_EXISTS, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.COLOR_SAME_HEXA_CODE_ALREADY_EXISTS.getDescription(), error.getMessage());
	}
	
	@Test
	void givenInternalError_whenPostCreate_thenGenericError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		String hexaCodeToCreate = "#000000";
		HttpEntity<String> request = new HttpEntity<String>(hexaCodeToCreate, new HttpHeaders());
		
		when(colorV2Endpoint.createColor(hexaCodeToCreate))
			.thenThrow(new IllegalArgumentException("Test"));
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiTechnicalErrorDto> response = restTemplate.postForEntity(serverPortUrl, request, ApiTechnicalErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
	@Test
	@Sql("classpath:api-v2/color/delete_deleteById_createSeveralColors.sql")
	void givenValidColor_whenDeleteDeleteById_thenColorDeleted() {
		// Given
		String serverPortUrl = String.format(DELETE_BY_ID_API_URL_PATTERN, serverPort);
		
		String colorIdToDelete = "id_1";
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(serverPortUrl, HttpMethod.DELETE, null, String.class, colorIdToDelete);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		List<ColorDto> allReminaingColors = colorV2Endpoint.getAllAvailableColors();
		assertEquals(1, allReminaingColors.size());

		ColorDto remainingColor = allReminaingColors.get(0);
		assertEquals("id_2", remainingColor.getId());
		assertEquals("#000001", remainingColor.getHexaCode());
	}
	
	@Test
	void givenInternalError_whenDeleteDeleteById_thenGenericError() {
		// Given
		String serverPortUrl = String.format(DELETE_BY_ID_API_URL_PATTERN, serverPort);
		
		String colorIdToDelete = "anything";
		
		doThrow(new IllegalArgumentException("Test"))
			.when(colorV2Endpoint)
			.deleteColor(anyString());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiTechnicalErrorDto> response = restTemplate.exchange(serverPortUrl, HttpMethod.DELETE, null, ApiTechnicalErrorDto.class, colorIdToDelete);
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
}