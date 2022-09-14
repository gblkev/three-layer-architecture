package com.gebel.threelayerarchitecture.controller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SwaggerIT extends AbstractIntegrationTest {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	void givenSwaggerUiEnabled_whenAccessingSwaggerUi_thenHttpResponseOk() {
		// Given
		String swaggerUiUrlPattern = "http://localhost:%d/actuator/swagger-ui";
		String serverPortUrl = String.format(swaggerUiUrlPattern, getManagementPort());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(serverPortUrl, String.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void givenV1OpenApiDocEnabled_whenAccessingV1OpenApiDocEndpoint_thenValidContent() throws Exception {
		// Given
		String swaggerUiUrlPattern = "http://localhost:%d/actuator/openapi/v1";
		String serverPortUrl = String.format(swaggerUiUrlPattern, getManagementPort());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(serverPortUrl, String.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		JsonNode responseAsJsonNode = mapper.readTree(response.getBody());
		
		assertV1ApiInfo(responseAsJsonNode);
		assertV1ApiPaths(responseAsJsonNode);
		assertV1ApiSchemas(responseAsJsonNode);
	}
	
	private void assertV1ApiInfo(JsonNode responseAsJsonNode) {
		JsonNode infoNode = responseAsJsonNode.get("info");
		assertEquals("Cars management API", infoNode.get("title").asText());
		assertEquals("Allows to manage cars and drivers", infoNode.get("description").asText());
		assertEquals("v1", infoNode.get("version").asText());
	}
	
	private void assertV1ApiPaths(JsonNode responseAsJsonNode) {
		JsonNode pathsNode = responseAsJsonNode.get("paths");
		List<String> paths = List.of(
			"/api/v1/cars",
			"/api/v1/cars/{carId}",
			"/api/v1/colors",
			"/api/v1/colors/{colorId}",
			"/api/v1/drivers",
			"/api/v1/drivers/{driverId}");
		assertEquals(paths, IteratorUtils.toList(pathsNode.fieldNames()));
	}
	
	private void assertV1ApiSchemas(JsonNode responseAsJsonNode) {
		JsonNode schemasNode = responseAsJsonNode.get("components").get("schemas");
		List<String> schemas = List.of(
			"ApiBusinessErrorDto",
			"ApiTechnicalErrorDto",
			"CarDto",
			"ColorDto",
			"CreateCarDto",
			"CreateDriverDto",
			"DriverDto");
		assertEquals(schemas, IteratorUtils.toList(schemasNode.fieldNames()));
	}
	
	@Test
	void givenV2OpenApiDocEnabled_whenAccessingV2OpenApiDocEndpoint_thenValidContent() throws Exception {
		// Given
		String swaggerUiUrlPattern = "http://localhost:%d/actuator/openapi/v2";
		String serverPortUrl = String.format(swaggerUiUrlPattern, getManagementPort());
		
		// When
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(serverPortUrl, String.class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		JsonNode responseAsJsonNode = mapper.readTree(response.getBody());
		
		assertV2ApiInfo(responseAsJsonNode);
		assertV2ApiPaths(responseAsJsonNode);
		assertV2ApiSchemas(responseAsJsonNode);
	}
	
	private void assertV2ApiInfo(JsonNode responseAsJsonNode) {
		JsonNode infoNode = responseAsJsonNode.get("info");
		assertEquals("Cars management API", infoNode.get("title").asText());
		assertEquals("Allows to manage cars and drivers", infoNode.get("description").asText());
		assertEquals("v2", infoNode.get("version").asText());
	}
	
	private void assertV2ApiPaths(JsonNode responseAsJsonNode) {
		JsonNode pathsNode = responseAsJsonNode.get("paths");
		List<String> paths = List.of(
			"/api/v2/cars",
			"/api/v2/cars/{carId}",
			"/api/v2/colors",
			"/api/v2/colors/{colorId}",
			"/api/v2/drivers",
			"/api/v2/drivers/{driverId}");
		assertEquals(paths, IteratorUtils.toList(pathsNode.fieldNames()));
	}
	
	private void assertV2ApiSchemas(JsonNode responseAsJsonNode) {
		JsonNode schemasNode = responseAsJsonNode.get("components").get("schemas");
		List<String> schemas = List.of(
			"ApiBusinessErrorDto",
			"ApiTechnicalErrorDto",
			"CarDto",
			"ColorDto",
			"CreateCarDto",
			"CreateDriverDto",
			"DriverDto");
		assertEquals(schemas, IteratorUtils.toList(schemasNode.fieldNames()));
	}

}