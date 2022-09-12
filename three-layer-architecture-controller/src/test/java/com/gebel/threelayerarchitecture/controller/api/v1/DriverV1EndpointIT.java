package com.gebel.threelayerarchitecture.controller.api.v1;

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
import com.gebel.threelayerarchitecture.controller.api.v1.dto.CreateDriverDto;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.DriverDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorCodeDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.DriverV1Endpoint;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DriverV1EndpointIT {
	
	private static final TestContainersManager TEST_CONTAINERS_MANAGER = new TestContainersManager(); // Shared between all methods.
	private static final String API_URL_PATTERN = "http://localhost:%d/api/v1/drivers";
	private static final String DELETE_BY_ID_API_URL_PATTERN = API_URL_PATTERN + "/{driverId}";
	
	@LocalServerPort
	private int serverPort;
	
	@SpyBean
	private DriverV1Endpoint driverV1Endpoint;
	
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
		TEST_CONTAINERS_MANAGER.getTestContainers().getMysqlDatabaseTestContainer().executeSqlScript("api-v1/driver/deleteAllDrivers.sql");
	}
	
	@Test
	@Sql("classpath:api-v1/driver/get_findAll_createSeveralDrivers.sql")
	void givenSeveralDrivers_whenGetFindAll_thenAllDriversRetrieved() {
		// Given + sql
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<DriverDto[]> response = restTemplate.getForEntity(serverPortUrl, DriverDto[].class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		DriverDto[] drivers = response.getBody();
		assertEquals(3, drivers.length);

		DriverDto driver1 = drivers[0];
		assertEquals("id_1", driver1.getId());
		assertEquals("Forrest", driver1.getFirstName());
		assertEquals("Gump", driver1.getLastName());

		DriverDto driver2 = drivers[1];
		assertEquals("id_2", driver2.getId());
		assertEquals("Tom", driver2.getFirstName());
		assertEquals("Hanks", driver2.getLastName());

		DriverDto driver3 = drivers[2];
		assertEquals("id_3", driver3.getId());
		assertEquals("Robert", driver3.getFirstName());
		assertEquals("Zemeckis", driver3.getLastName());
	}
	
	@Test
	void givenInternalError_whenGetFindAll_thenGenericError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		when(driverV1Endpoint.getAllDrivers())
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
	void givenValidDriver_whenPostCreate_thenDriverCreated() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		CreateDriverDto createDriverDto = CreateDriverDto.builder()
			.firstName("Forrest")
			.lastName("Gump")
			.build();
		HttpEntity<CreateDriverDto> request = new HttpEntity<>(createDriverDto, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<DriverDto> response = restTemplate.postForEntity(serverPortUrl, request, DriverDto.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		DriverDto createdDriver = response.getBody();
		assertNotNull(createdDriver.getId());
		assertEquals("Forrest", createdDriver.getFirstName());
		assertEquals("Gump", createdDriver.getLastName());
	}
	
	@Test
	void givenInvalidFirstName_whenPostCreate_thenInvalidFirstNameError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		CreateDriverDto createDriverDto = CreateDriverDto.builder()
			.firstName(null)
			.lastName("Gump")
			.build();
		HttpEntity<CreateDriverDto> request = new HttpEntity<>(createDriverDto, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(serverPortUrl, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.DRIVER_INVALID_FIRST_NAME, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.DRIVER_INVALID_FIRST_NAME.getDescription(), error.getMessage());
	}
	
	@Test
	void givenInvalidLastName_whenPostCreate_thenInvalidLastNameError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		CreateDriverDto createDriverDto = CreateDriverDto.builder()
			.firstName("Forrest")
			.lastName(null)
			.build();
		HttpEntity<CreateDriverDto> request = new HttpEntity<>(createDriverDto, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(serverPortUrl, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.DRIVER_INVALID_LAST_NAME, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.DRIVER_INVALID_LAST_NAME.getDescription(), error.getMessage());
	}
	
	@Test
	void givenInternalError_whenPostCreate_thenGenericError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		CreateDriverDto createDriverDto = CreateDriverDto.builder()
			.firstName("Forrest")
			.lastName("Gump")
			.build();
		HttpEntity<CreateDriverDto> request = new HttpEntity<>(createDriverDto, new HttpHeaders());
		
		when(driverV1Endpoint.createDriver(createDriverDto))
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
	@Sql("classpath:api-v1/driver/delete_deleteById_createSeveralDrivers.sql")
	void givenValidDriver_whenDeleteDeleteById_thenDriverDeleted() {
		// Given
		String serverPortUrl = String.format(DELETE_BY_ID_API_URL_PATTERN, serverPort);
		
		String driverIdToDelete = "id_1";
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(serverPortUrl, HttpMethod.DELETE, null, String.class, driverIdToDelete);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		List<DriverDto> allReminaingDrivers = driverV1Endpoint.getAllDrivers();
		assertEquals(1, allReminaingDrivers.size());

		DriverDto remainingDriver = allReminaingDrivers.get(0);
		assertEquals("id_2", remainingDriver.getId());
		assertEquals("Tom", remainingDriver.getFirstName());
		assertEquals("Hanks", remainingDriver.getLastName());
	}
	
	@Test
	void givenInternalError_whenDeleteDeleteById_thenGenericError() {
		// Given
		String serverPortUrl = String.format(DELETE_BY_ID_API_URL_PATTERN, serverPort);
		
		String driverIdToDelete = "anything";
		
		doThrow(new IllegalArgumentException("Test"))
			.when(driverV1Endpoint)
			.deleteDriver(anyString());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiTechnicalErrorDto> response = restTemplate.exchange(serverPortUrl, HttpMethod.DELETE, null, ApiTechnicalErrorDto.class, driverIdToDelete);
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
}