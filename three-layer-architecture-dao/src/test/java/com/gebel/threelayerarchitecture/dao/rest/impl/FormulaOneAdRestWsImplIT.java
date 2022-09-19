package com.gebel.threelayerarchitecture.dao.rest.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.gebel.threelayerarchitecture.dao._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.dao.rest.dto.FormulaOneAdDto;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class FormulaOneAdRestWsImplIT extends AbstractIntegrationTest {
	
	@Autowired
	private FormulaOneAdRestWsImpl formulaOneAdRestWs;
	
	@Test
	void givenSeveralAds_whenGetPersonalizedAds_thenAdsRetrieved() {
		// Given
		getTestContainers().getRestServicesTestContainer().mockFormulaOneAdRestWs_getPersonalizedAds("rest/formulaOneAd/getPersonalizedAds_severalAds.json");
		String driverId = "test-driver-id";
		
		// When
		List<FormulaOneAdDto> ads = formulaOneAdRestWs.getPersonalizedAds(driverId);
		
		// Then
		assertEquals(2, ads.size());
		
		FormulaOneAdDto ad1 = ads.get(0);
		assertEquals("50708586-035c-459e-b79a-d8d8e99292b2", ad1.getId());
		assertEquals(LocalDateTime.parse("2099-06-05T07:23:47.127"), ad1.getExpirationDate());
		assertEquals("This is a personalized ad just for you!", ad1.getMessage());
		
		FormulaOneAdDto ad2 = ads.get(1);
		assertEquals("99d59ac2-e557-4866-af5b-c3b721ba247f", ad2.getId());
		assertEquals(LocalDateTime.parse("2054-12-04T18:47:38.927"), ad2.getExpirationDate());
		assertEquals("If you like formula one, you gonna love the new Ferrari! Click here", ad2.getMessage());
	}
	
	@Test
	void givenApiAvailable_whenUnsubscribePersonalizedAds_thenNoError() {
		// Given
		getTestContainers().getRestServicesTestContainer().mockFormulaOneAdRestWs_unsubscribe();
		String driverId = "test-driver-id";
		
		// When
		formulaOneAdRestWs.unsubscribePersonalizedAds(driverId);
		
		// Then no error
	}
	
}