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
import org.springframework.transaction.annotation.Transactional;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.CarDto;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.CreateCarDto;
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
class CarV2EndpointIT extends AbstractIntegrationTest {
	
	private static final String CARS_API_URL_PATTERN = "http://localhost:%d/api/v2/cars";
	private static final String CARS_DELETE_BY_ID_API_URL_PATTERN = CARS_API_URL_PATTERN + "/{carId}";
	private static final String COLORS_API_URL_PATTERN = "http://localhost:%d/api/v2/colors";
	private static final String DRIVERS_API_URL_PATTERN = "http://localhost:%d/api/v2/drivers";
	
	private final TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	void givenSeveralCars_whenGetFindAll_thenAllCarsRetrieved() throws Exception {
		// Given
		getTestContainers().getMysqlTestContainer().executeSqlScript("api/v2/car/get_findAll_createSeveralCars.sql");
		String url = String.format(CARS_API_URL_PATTERN, getServerPort());
		
		// When
		ResponseEntity<CarDto[]> response = restTemplate.getForEntity(url, CarDto[].class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		CarDto[] cars = response.getBody();
		assertEquals(3, cars.length);

		CarDto car1 = cars[0];
		assertEquals("car_id_1", car1.getId());
		ColorDto color1 = car1.getColor();
		assertEquals("color_id_1", color1.getId());
		assertEquals("#000000", color1.getHexaCode());
		DriverDto driver1 = car1.getDriver();
		assertEquals("driver_id_1", driver1.getId());
		assertEquals("Forrest", driver1.getFirstName());
		assertEquals("Gump", driver1.getLastName());

		CarDto car2 = cars[1];
		assertEquals("car_id_2", car2.getId());
		ColorDto color2 = car2.getColor();
		assertEquals("color_id_2", color2.getId());
		assertEquals("#000001", color2.getHexaCode());
		DriverDto driver2 = car2.getDriver();
		assertEquals("driver_id_2", driver2.getId());
		assertEquals("Tom", driver2.getFirstName());
		assertEquals("Hanks", driver2.getLastName());

		CarDto car3 = cars[2];
		assertEquals("car_id_3", car3.getId());
		ColorDto color3 = car3.getColor();
		assertEquals("color_id_3", color3.getId());
		assertEquals("#000002", color3.getHexaCode());
		DriverDto driver3 = car3.getDriver();
		assertEquals("driver_id_3", driver3.getId());
		assertEquals("Robert", driver3.getFirstName());
		assertEquals("Zemeckis", driver3.getLastName());
	}
	
	@Test
	@Transactional // To get a connection using the specific datasource configuration we defined on top of the class.
	void givenDatabaseUnavailable_whenGetFindAll_thenGenericError() {
		// Given
		String url = String.format(CARS_API_URL_PATTERN, getServerPort());
		
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
	void givenValidCar_whenPostCreate_thenCarCreated() throws Exception {
		// Given
		getTestContainers().getMysqlTestContainer().executeSqlScript("api/v2/car/post_create_createColorAndDriver.sql");
		String url = String.format(CARS_API_URL_PATTERN, getServerPort());
		
		CreateCarDto createCarDto = CreateCarDto.builder()
			.colorId("color_id")
			.driverId("driver_id")
			.build();
		HttpEntity<CreateCarDto> request = new HttpEntity<>(createCarDto, new HttpHeaders());
		
		// When
		ResponseEntity<CarDto> response = restTemplate.postForEntity(url, request, CarDto.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		CarDto createdCar = response.getBody();
		assertNotNull(createdCar.getId());
		ColorDto createdCarColor = createdCar.getColor();
		assertEquals("color_id", createdCarColor.getId());
		assertEquals("#000000", createdCarColor.getHexaCode());
		DriverDto createdCarDriver = createdCar.getDriver();
		assertEquals("driver_id", createdCarDriver.getId());
		assertEquals("Forrest", createdCarDriver.getFirstName());
		assertEquals("Gump", createdCarDriver.getLastName());
	}
	
	@Test
	void givenInvalidColor_whenPostCreate_thenInvalidColorError() {
		// Given
		String url = String.format(CARS_API_URL_PATTERN, getServerPort());
		
		CreateCarDto createCarDto = CreateCarDto.builder()
			.colorId(null)
			.driverId("driver_id")
			.build();
		HttpEntity<CreateCarDto> request = new HttpEntity<>(createCarDto, new HttpHeaders());
		
		// When
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(url, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.CAR_INVALID_COLOR, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.CAR_INVALID_COLOR.getDescription(), error.getMessage());
	}
	
	@Test
	void givenInvalidDriver_whenPostCreate_thenInvalidDriverError() throws Exception {
		// Given
		getTestContainers().getMysqlTestContainer().executeSqlScript("api/v2/car/post_create_createValidColor.sql");
		String url = String.format(CARS_API_URL_PATTERN, getServerPort());
		
		CreateCarDto createCarDto = CreateCarDto.builder()
			.colorId("color_id")
			.driverId(null)
			.build();
		HttpEntity<CreateCarDto> request = new HttpEntity<>(createCarDto, new HttpHeaders());
		
		// When
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(url, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.CAR_INVALID_DRIVER, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.CAR_INVALID_DRIVER.getDescription(), error.getMessage());
	}
	
	@Test
	@Transactional // To get a connection using the specific datasource configuration we defined on top of the class.
	void givenDatabaseUnavailable_whenPostCreate_thenGenericError() {
		// Given
		String url = String.format(CARS_API_URL_PATTERN, getServerPort());
		
		CreateCarDto createCarDto = CreateCarDto.builder()
			.colorId("color_id")
			.driverId("driver_id")
			.build();
		HttpEntity<CreateCarDto> request = new HttpEntity<>(createCarDto, new HttpHeaders());
		
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
	void givenValidCar_whenDeleteDeleteById_thenCarDeleted() throws Exception {
		// Given
		getTestContainers().getMysqlTestContainer().executeSqlScript("api/v2/car/delete_deleteById_createSeveralCars.sql");
		String url = String.format(CARS_DELETE_BY_ID_API_URL_PATTERN, getServerPort());
		
		String carIdToDelete = "car_id_1";
		
		// When
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class, carIdToDelete);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		List<CarDto> allReminaingCars = getAllCars();
		assertEquals(1, allReminaingCars.size());
		CarDto remainingCar = allReminaingCars.get(0);
		assertEquals("car_id_2", remainingCar.getId());
		
		List<ColorDto> allReminaingColors = getAllColors();
		assertEquals(2, allReminaingColors.size());
		ColorDto remainingColor1 = allReminaingColors.get(0);
		assertEquals("color_id_1", remainingColor1.getId());
		assertEquals("#000000", remainingColor1.getHexaCode());
		ColorDto remainingColor2 = allReminaingColors.get(1);
		assertEquals("color_id_2", remainingColor2.getId());
		assertEquals("#000001", remainingColor2.getHexaCode());
		
		List<DriverDto> allReminaingDrivers = getAllDrivers();
		assertEquals(2, allReminaingDrivers.size());
		DriverDto remainingDriver1 = allReminaingDrivers.get(0);
		assertEquals("driver_id_1", remainingDriver1.getId());
		assertEquals("Forrest", remainingDriver1.getFirstName());
		assertEquals("Gump", remainingDriver1.getLastName());
		DriverDto remainingDriver2 = allReminaingDrivers.get(1);
		assertEquals("driver_id_2", remainingDriver2.getId());
		assertEquals("Tom", remainingDriver2.getFirstName());
		assertEquals("Hanks", remainingDriver2.getLastName());
	}
	
	private List<CarDto> getAllCars() {
		String findAllUrl = String.format(CARS_API_URL_PATTERN, getServerPort());
		ResponseEntity<CarDto[]> findAllResponse = restTemplate.getForEntity(findAllUrl, CarDto[].class);
		return Arrays.asList(findAllResponse.getBody());
	}
	
	private List<ColorDto> getAllColors() {
		String findAllUrl = String.format(COLORS_API_URL_PATTERN, getServerPort());
		ResponseEntity<ColorDto[]> findAllResponse = restTemplate.getForEntity(findAllUrl, ColorDto[].class);
		return Arrays.asList(findAllResponse.getBody());
	}
	
	private List<DriverDto> getAllDrivers() {
		String findAllUrl = String.format(DRIVERS_API_URL_PATTERN, getServerPort());
		ResponseEntity<DriverDto[]> findAllResponse = restTemplate.getForEntity(findAllUrl, DriverDto[].class);
		return Arrays.asList(findAllResponse.getBody());
	}
	
	@Test
	@Transactional // To get a connection using the specific datasource configuration we defined on top of the class.
	void givenDatabaseUnavailable_whenDeleteDeleteById_thenGenericError() {
		// Given
		String url = String.format(CARS_DELETE_BY_ID_API_URL_PATTERN, getServerPort());
		
		String carIdToDelete = "anything";
		
		// When
		ResponseEntity<ApiTechnicalErrorDto> response;
		try {
			getTestContainers().getMysqlTestContainer().pause();
			response = restTemplate.exchange(url, HttpMethod.DELETE, null, ApiTechnicalErrorDto.class, carIdToDelete);
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