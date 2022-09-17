package com.gebel.threelayerarchitecture.controller.api.v2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.BrandDto;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ModelDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.ApiTechnicalErrorDto;

@SpringBootTest(
	webEnvironment = WebEnvironment.RANDOM_PORT,
	// To speed up the error occurrence when we simulate a connection issue with redis.
	properties = "spring.redis.timeout=250"
)
class BrandV2EndpointIT extends AbstractIntegrationTest {
	
	private static final String API_URL_PATTERN = "http://localhost:%d/api/v2/brands";

	private final TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	void givenSeveralBrands_whenGetFindAll_thenAllBrandsRetrieved() throws Exception {
		// Given
		getTestContainers().getRedisTestContainer().executeCommandsScript("api/v2/brand/get_findAll_createSeveralBrands");
		String url = String.format(API_URL_PATTERN, getServerPort());
		
		// When
		ResponseEntity<BrandDto[]> response = restTemplate.getForEntity(url, BrandDto[].class);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		BrandDto brand1 = new BrandDto();
		brand1.setId("daef3c81-e459-4306-83c7-3fd7bf208c31");
		brand1.setName("Ferrari");
		ModelDto brand1Model1 = new ModelDto("e3e4b076-b8fb-4b84-b7ff-68ca41a7ac14", "328 GTS");
		ModelDto brand1Model2 = new ModelDto("71552d52-6765-4a4f-bf0b-77bf49d42431", "F430");
		brand1.setModels(List.of(brand1Model1, brand1Model2));
		
		BrandDto brand2 = new BrandDto();
		brand2.setId("ca7a30a5-79b9-410a-a9ac-592aa6b48e0e");
		brand2.setName("Audi");
		ModelDto brand2Model1 = new ModelDto("232dc99c-9f4b-40a6-aa50-913450bd8378", "A1 Sportback");
		brand2.setModels(List.of(brand2Model1));
		
		BrandDto brand3 = new BrandDto();
		brand3.setId("e81e19fd-b1ae-46ad-b667-0f954720c085");
		brand3.setName("Renault");
		ModelDto brand3Model1 = new ModelDto("4beeb268-c970-4c36-8efe-c2861672ef77", "Clio 5");
		ModelDto brand3Model2 = new ModelDto("dc9dae40-a0d5-4b6d-8067-4f22e7e9fa49", "Megane");
		ModelDto brand3Model3 = new ModelDto("5732cac2-12d5-40f3-b0f1-10f75ea3ef74", "Twingo");
		ModelDto brand3Model4 = new ModelDto("15b129aa-75c2-48ed-80f0-32f58dfbad07", "Captur");
		brand3.setModels(List.of(brand3Model1, brand3Model2, brand3Model3, brand3Model4));

		Set<BrandDto> expectedBrands = Set.of(brand1, brand2, brand3);
		Set<BrandDto> brands = new HashSet<>(Arrays.asList(response.getBody()));
		assertEquals(expectedBrands, brands);
	}
	
	@Test
	void givenDatabaseUnavailable_whenGetFindAll_thenGenericError() {
		// Given
		String url = String.format(API_URL_PATTERN, getServerPort());
		
		// When
		ResponseEntity<ApiTechnicalErrorDto> response;
		try {
			getTestContainers().getRedisTestContainer().pause();
			response = restTemplate.getForEntity(url, ApiTechnicalErrorDto.class);
		}
		finally {
			getTestContainers().getRedisTestContainer().unpause();
		}
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
}