package com.gebel.threelayerarchitecture.controller.api.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

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
import org.springframework.test.context.jdbc.Sql;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.CarDto;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.CreateCarDto;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.DriverDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorCodeDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.CarV1Endpoint;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.ColorV1Endpoint;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.DriverV1Endpoint;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CarV1EndpointIT extends AbstractIntegrationTest {
	
	private static final String API_URL_PATTERN = "http://localhost:%d/api/v1/cars";
	private static final String DELETE_BY_ID_API_URL_PATTERN = API_URL_PATTERN + "/{carId}";
	
	@LocalServerPort
	private int serverPort;
	
	@SpyBean
	private CarV1Endpoint carV1Endpoint;
	
	@SpyBean
	private ColorV1Endpoint colorV1Endpoint;
	
	@SpyBean
	private DriverV1Endpoint driverV1Endpoint;
	
	@Test
	@Sql("classpath:api-v1/car/get_findAll_createSeveralCars.sql")
	void givenSeveralCars_whenGetFindAll_thenAllCarsRetrieved() {
		// Given + sql
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<CarDto[]> response = restTemplate.getForEntity(serverPortUrl, CarDto[].class);
		
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
	void givenInternalError_whenGetFindAll_thenGenericError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		when(carV1Endpoint.getAllCars())
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
	@Sql("classpath:api-v1/car/post_create_createColorAndDriver.sql")
	void givenValidCar_whenPostCreate_thenCarCreated() {
		// Given + sql
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		CreateCarDto createCarDto = CreateCarDto.builder()
			.colorId("color_id")
			.driverId("driver_id")
			.build();
		HttpEntity<CreateCarDto> request = new HttpEntity<>(createCarDto, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<CarDto> response = restTemplate.postForEntity(serverPortUrl, request, CarDto.class);
		
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
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		CreateCarDto createCarDto = CreateCarDto.builder()
			.colorId(null)
			.driverId("driver_id")
			.build();
		HttpEntity<CreateCarDto> request = new HttpEntity<>(createCarDto, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(serverPortUrl, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.CAR_INVALID_COLOR, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.CAR_INVALID_COLOR.getDescription(), error.getMessage());
	}
	
	@Test
	@Sql("classpath:api-v1/car/post_create_createValidColor.sql")
	void givenInvalidDriver_whenPostCreate_thenInvalidDriverError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		CreateCarDto createCarDto = CreateCarDto.builder()
			.colorId("color_id")
			.driverId(null)
			.build();
		HttpEntity<CreateCarDto> request = new HttpEntity<>(createCarDto, new HttpHeaders());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiBusinessErrorDto> response = restTemplate.postForEntity(serverPortUrl, request, ApiBusinessErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
		ApiBusinessErrorDto error = response.getBody();
		assertEquals(ApiBusinessErrorCodeDto.CAR_INVALID_DRIVER, error.getErrorCode());
		assertEquals(ApiBusinessErrorCodeDto.CAR_INVALID_DRIVER.getDescription(), error.getMessage());
	}
	
	@Test
	void givenInternalError_whenPostCreate_thenGenericError() {
		// Given
		String serverPortUrl = String.format(API_URL_PATTERN, serverPort);
		
		CreateCarDto createCarDto = CreateCarDto.builder()
			.colorId("color_id")
			.driverId("driver_id")
			.build();
		HttpEntity<CreateCarDto> request = new HttpEntity<>(createCarDto, new HttpHeaders());
		
		doThrow(new IllegalArgumentException("Test"))
			.when(carV1Endpoint)
			.createCar(createCarDto);
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiTechnicalErrorDto> response = restTemplate.postForEntity(serverPortUrl, request, ApiTechnicalErrorDto.class);
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
	@Test
	@Sql("classpath:api-v1/car/delete_deleteById_createSeveralCars.sql")
	void givenValidCar_whenDeleteDeleteById_thenCarDeleted() {
		// Given
		String serverPortUrl = String.format(DELETE_BY_ID_API_URL_PATTERN, serverPort);
		
		String carIdToDelete = "car_id_1";
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(serverPortUrl, HttpMethod.DELETE, null, String.class, carIdToDelete);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		List<CarDto> allReminaingCars = carV1Endpoint.getAllCars();
		assertEquals(1, allReminaingCars.size());
		CarDto remainingCar = allReminaingCars.get(0);
		assertEquals("car_id_2", remainingCar.getId());
		
		List<ColorDto> allReminaingColors = colorV1Endpoint.getAllColors();
		assertEquals(2, allReminaingColors.size());
		ColorDto remainingColor1 = allReminaingColors.get(0);
		assertEquals("color_id_1", remainingColor1.getId());
		assertEquals("#000000", remainingColor1.getHexaCode());
		ColorDto remainingColor2 = allReminaingColors.get(1);
		assertEquals("color_id_2", remainingColor2.getId());
		assertEquals("#000001", remainingColor2.getHexaCode());
		
		List<DriverDto> allReminaingDrivers = driverV1Endpoint.getAllDrivers();
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
	
	@Test
	void givenInternalError_whenDeleteDeleteById_thenGenericError() {
		// Given
		String serverPortUrl = String.format(DELETE_BY_ID_API_URL_PATTERN, serverPort);
		
		String carIdToDelete = "anything";
		
		doThrow(new IllegalArgumentException("Test"))
			.when(carV1Endpoint)
			.deleteCar(anyString());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<ApiTechnicalErrorDto> response = restTemplate.exchange(serverPortUrl, HttpMethod.DELETE, null, ApiTechnicalErrorDto.class, carIdToDelete);
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
}