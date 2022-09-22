package com.gebel.threelayerarchitecture.dao.rest.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.gebel.threelayerarchitecture.dao._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.dao.rest.dto.SportAdDto;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class SportAdRestWsImplIT extends AbstractIntegrationTest {
	
	@Autowired
	private SportAdRestWsImpl sportAdRestWs;
	
	@Test
	void givenSeveralAds_whenGetPersonalizedAds_thenAdsRetrieved() {
		// Given
		getTestContainers().getRestServicesTestContainer().mockSportAdRestWs_getPersonalizedAds("rest/sportAd/getPersonalizedAds_severalAds.json");
		String driverId = "test-driver-id";
		
		// When
		List<SportAdDto> ads = sportAdRestWs.getPersonalizedAds(driverId);
		
		// Then
		assertEquals(3, ads.size());
		
		SportAdDto ad1 = ads.get(0);
		assertEquals("1d77e96d-469f-49ec-ab53-54878d71a9f6", ad1.getId());
		assertEquals(LocalDateTime.parse("2065-04-25T23:19:59.177"), ad1.getExpirationDate());
		assertEquals("Today's match: Manchester United 3 - 0 Liverpool", ad1.getMessage());
		
		SportAdDto ad2 = ads.get(1);
		assertEquals("5895191e-8bd1-498d-b7ca-2adef0f26a27", ad2.getId());
		assertEquals(LocalDateTime.parse("2051-01-12T10:09:14.911"), ad2.getExpirationDate());
		assertEquals("Federer's retirement: a huge shock", ad2.getMessage());
		
		SportAdDto ad3 = ads.get(2);
		assertEquals("78e140a6-e1f2-4649-86db-42384c30a930", ad3.getId());
		assertEquals(LocalDateTime.parse("2079-03-31T08:03:00.456"), ad3.getExpirationDate());
		assertEquals("Michael Jordan's back on the court", ad3.getMessage());
	}
	
	@Test
	void givenApiAvailable_whenUnsubscribePersonalizedAds_thenNoError() {
		// Given
		getTestContainers().getRestServicesTestContainer().mockSportAdRestWs_unsubscribe();
		String driverId = "test-driver-id";
		
		// When
		sportAdRestWs.unsubscribePersonalizedAds(driverId);
		
		// Then no error
	}
	
}