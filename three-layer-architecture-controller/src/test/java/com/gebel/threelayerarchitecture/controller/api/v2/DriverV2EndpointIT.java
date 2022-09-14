package com.gebel.threelayerarchitecture.controller.api.v2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.CreateDriverDto;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.DriverDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorCodeDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiTechnicalErrorDto;

@SpringBootTest(
	webEnvironment = WebEnvironment.RANDOM_PORT,
	// To speed up the error occurrence when we simulate a connection issue with the database.
	properties = {
		"spring.datasource.hikari.connection-timeout=250",
		"spring.datasource.hikari.validation-timeout=250"
	}
)
class DriverV2EndpointIT extends AbstractIntegrationTest {
	
	private static final String API_URL_PATTERN = "http://localhost:%d/api/v2/drivers";
	private static final String DELETE_BY_ID_API_URL_PATTERN = API_URL_PATTERN + "/{driverId}";
	
	private final TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	@Sql("classpath:api-v2/driver/get_findAll_createSeveralDrivers.sql")
	void givenSeveralDrivers_whenGetFindAll_thenAllDriversRetrieved() {
		// Given + sql
		String url = String.format(API_URL_PATTERN, getServerPort());
		
		// When
		ResponseEntity<DriverDto[]> response = restTemplate.getForEntity(url, DriverDto[].class);
		
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
	@Transactional // To get a connection using the specific datasource configuration we defined on top of the class.
	void givenDatabaseUnavailable_whenGetFindAll_thenGenericError() {
		// Given
		String url = String.format(API_URL_PATTERN, getServerPort());
		
		// When
		ResponseEntity<ApiTechnicalErrorDto> response;
		try {
			getTestContainers().getMysqlTestContainer().pause();
			response = restTemplate.getForEntity(url, ApiTechnicalErrorDto.class);
		}
		finally {
			getTestContainers().getMysqlTestContainer().unpause();
		}
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
	@Test
	void givenValidDriver_whenPostCreate_thenDriverCreated() {
		// Given
		String url = String.format(API_URL_PATTERN, getServerPort());
		
		CreateDriverDto createDriverDto = CreateDriverDto.builder()
			.firstName("Forrest")
			.lastName("Gump")
			.build();
		HttpEntity<CreateDriverDto> request = new HttpEntity<>(createDriverDto, new HttpHeaders());
		
		// When
		ResponseEntity<DriverDto> response = restTemplate.postForEntity(url, request, DriverDto.class);
		
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
		String url = String.format(API_URL_PATTERN, getServerPort());
		
		CreateDriverDto createDriverDto = CreateDriverDto.builder()
			.firstName(null)
			.lastName("Gump")
			.build();
		HttpEntity<CreateDriverDto> request = new HttpEntity<>(createDriverDto, new HttpHeaders());
		
		// When
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(url, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.DRIVER_INVALID_FIRST_NAME, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.DRIVER_INVALID_FIRST_NAME.getDescription(), error.getMessage());
	}
	
	@Test
	void givenInvalidLastName_whenPostCreate_thenInvalidLastNameError() {
		// Given
		String url = String.format(API_URL_PATTERN, getServerPort());
		
		CreateDriverDto createDriverDto = CreateDriverDto.builder()
			.firstName("Forrest")
			.lastName(null)
			.build();
		HttpEntity<CreateDriverDto> request = new HttpEntity<>(createDriverDto, new HttpHeaders());
		
		// When
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(url, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.DRIVER_INVALID_LAST_NAME, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.DRIVER_INVALID_LAST_NAME.getDescription(), error.getMessage());
	}
	
	@Test
	@Transactional // To get a connection using the specific datasource configuration we defined on top of the class.
	void givenDatabaseUnavailable_whenPostCreate_thenGenericError() {
		// Given
		String url = String.format(API_URL_PATTERN, getServerPort());
		
		CreateDriverDto createDriverDto = CreateDriverDto.builder()
			.firstName("Forrest")
			.lastName("Gump")
			.build();
		HttpEntity<CreateDriverDto> request = new HttpEntity<>(createDriverDto, new HttpHeaders());
		
		// When
		ResponseEntity<ApiTechnicalErrorDto> response;
		try {
			getTestContainers().getMysqlTestContainer().pause();
			response = restTemplate.postForEntity(url, request, ApiTechnicalErrorDto.class);
		}
		finally {
			getTestContainers().getMysqlTestContainer().unpause();
		}
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
	@Test
	@Sql("classpath:api-v2/driver/delete_deleteById_createSeveralDrivers.sql")
	void givenValidDriver_whenDeleteDeleteById_thenDriverDeleted() {
		// Given
		String url = String.format(DELETE_BY_ID_API_URL_PATTERN, getServerPort());
		
		String driverIdToDelete = "id_1";
		
		// When
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class, driverIdToDelete);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		List<DriverDto> remainingDrivers = getAllDrivers();
		assertEquals(1, remainingDrivers.size());

		DriverDto remainingDriver = remainingDrivers.get(0);
		assertEquals("id_2", remainingDriver.getId());
		assertEquals("Tom", remainingDriver.getFirstName());
		assertEquals("Hanks", remainingDriver.getLastName());
	}
	
	private List<DriverDto> getAllDrivers() {
		String findAllUrl = String.format(API_URL_PATTERN, getServerPort());
		ResponseEntity<DriverDto[]> findAllResponse = restTemplate.getForEntity(findAllUrl, DriverDto[].class);
		return Arrays.asList(findAllResponse.getBody());
	}
	
	@Test
	@Transactional // To get a connection using the specific datasource configuration we defined on top of the class.
	void givenDatabaseUnavailable_whenDeleteDeleteById_thenGenericError() {
		// Given
		String url = String.format(DELETE_BY_ID_API_URL_PATTERN, getServerPort());
		
		String driverIdToDelete = "anything";
		
		// When
		ResponseEntity<ApiTechnicalErrorDto> response;
		try {
			getTestContainers().getMysqlTestContainer().pause();
			response = restTemplate.exchange(url, HttpMethod.DELETE, null, ApiTechnicalErrorDto.class, driverIdToDelete);
		}
		finally {
			getTestContainers().getMysqlTestContainer().unpause();
		}
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
}