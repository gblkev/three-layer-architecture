package com.gebel.threelayerarchitecture.controller.api.v2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.AdCategoryDto;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.AdDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.ApiTechnicalErrorDto;

@SpringBootTest(
	webEnvironment = WebEnvironment.RANDOM_PORT,
	// To speed up the error occurrence when we simulate a connection issue with a rest web service.
	properties = {
		"dao.rest.formula-one.ad.connect-timeout-in-millis=250",
		"dao.rest.sport.ad.connect-timeout-in-millis=250"
	}
)
class AdV2EndpointIT extends AbstractIntegrationTest {
	
	private static final String API_URL_PATTERN = "http://localhost:%d/api/v2/ads";
	private static final String GET_PERSONALIZED_ADS_API_URL_PATTERN = API_URL_PATTERN + "/{driverId}";
	private static final String UNSUBSCRIBE_PERSONALIZED_ADS_API_URL_PATTERN = API_URL_PATTERN + "/unsubscribe/{driverId}";

	private final TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	void givenSeveralAds_whenGetPersonalizedAds_thenAllAdsRetrieved() {
		// Given
		getTestContainers().getRestServicesTestContainer().mockFormulaOneAdRestWs_getPersonalizedAds("api/v2/ad/getPersonalizedAds_severalFormulaOneAds.json");
		getTestContainers().getRestServicesTestContainer().mockSportAdRestWs_getPersonalizedAds("api/v2/ad/getPersonalizedAds_severalSportAds.json");
		String url = String.format(GET_PERSONALIZED_ADS_API_URL_PATTERN, getServerPort());
		String driverId = "test-driver-id";
		
		// When
		ResponseEntity<AdDto[]> response = restTemplate.getForEntity(url, AdDto[].class, driverId);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		AdDto[] ads = response.getBody();
		assertEquals(5, ads.length);

		AdDto ad1 = ads[0];
		assertEquals("formulaone-50708586-035c-459e-b79a-d8d8e99292b2", ad1.getId());
		assertEquals(AdCategoryDto.FORMULA_ONE, ad1.getCategory());
		assertEquals("This is a personalized ad just for you!", ad1.getMessage());
		
		AdDto ad2 = ads[1];
		assertEquals("formulaone-99d59ac2-e557-4866-af5b-c3b721ba247f", ad2.getId());
		assertEquals(AdCategoryDto.FORMULA_ONE, ad2.getCategory());
		assertEquals("If you like formula one, you gonna love the new Ferrari! Click here", ad2.getMessage());
		
		AdDto ad3 = ads[2];
		assertEquals("sport-1d77e96d-469f-49ec-ab53-54878d71a9f6", ad3.getId());
		assertEquals(AdCategoryDto.SPORT, ad3.getCategory());
		assertEquals("Today's match: Manchester United 3 - 0 Liverpool", ad3.getMessage());
		
		AdDto ad4 = ads[3];
		assertEquals("sport-5895191e-8bd1-498d-b7ca-2adef0f26a27", ad4.getId());
		assertEquals(AdCategoryDto.SPORT, ad4.getCategory());
		assertEquals("Federer's retirement: a huge shock", ad4.getMessage());
		
		AdDto ad5 = ads[4];
		assertEquals("sport-78e140a6-e1f2-4649-86db-42384c30a930", ad5.getId());
		assertEquals(AdCategoryDto.SPORT, ad5.getCategory());
		assertEquals("Michael Jordan's back on the court", ad5.getMessage());
	}
	
	@Test
	void givenRestServicesUnavailable_whenGetPersonalizedAds_thenEmptyListReturned() {
		// Given
		String url = String.format(GET_PERSONALIZED_ADS_API_URL_PATTERN, getServerPort());
		String driverId = "test-driver-id";
		
		// When
		ResponseEntity<AdDto[]> response;
		try {
			getTestContainers().getRestServicesTestContainer().pause();
			response = restTemplate.getForEntity(url, AdDto[].class, driverId);
		}
		finally {
			getTestContainers().getRestServicesTestContainer().unpause();
		}
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0, response.getBody().length);
	}
	
	@Test
	void given_whenUnsubscribePersonalizedAds_thenNoError() {
		// Given
		getTestContainers().getRestServicesTestContainer().mockFormulaOneAdRestWs_unsubscribe();
		getTestContainers().getRestServicesTestContainer().mockSportAdRestWs_unsubscribe();
		String url = String.format(UNSUBSCRIBE_PERSONALIZED_ADS_API_URL_PATTERN, getServerPort());
		String driverId = "test-driver-id";
		
		// When
		ResponseEntity<?> response = restTemplate.postForEntity(url, HttpEntity.EMPTY, Void.class, driverId);
		
		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void givenRestServicesUnavailable_whenUnsubscribePersonalizedAds_thenGenericError() {
		// Given
		getTestContainers().getRestServicesTestContainer().mockFormulaOneAdRestWs_unsubscribe();
		getTestContainers().getRestServicesTestContainer().mockSportAdRestWs_unsubscribe();
		String url = String.format(UNSUBSCRIBE_PERSONALIZED_ADS_API_URL_PATTERN, getServerPort());
		String driverId = "test-driver-id";
		
		// When
		ResponseEntity<ApiTechnicalErrorDto> response;
		try {
			getTestContainers().getRestServicesTestContainer().pause();
			response = restTemplate.postForEntity(url, HttpEntity.EMPTY, ApiTechnicalErrorDto.class, driverId);
		}
		finally {
			getTestContainers().getRestServicesTestContainer().unpause();
		}
		
		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		
		ApiTechnicalErrorDto apiTechnicalErrorDto = response.getBody();
		assertEquals("An unexpected error occured", apiTechnicalErrorDto.getMessage());
	}
	
}