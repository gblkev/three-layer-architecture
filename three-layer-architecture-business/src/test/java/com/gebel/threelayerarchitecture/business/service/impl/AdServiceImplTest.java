package com.gebel.threelayerarchitecture.business.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gebel.threelayerarchitecture.business.domain.Ad;
import com.gebel.threelayerarchitecture.business.domain.AdCategory;
import com.gebel.threelayerarchitecture.business.service.converter.DomainAdConverter;
import com.gebel.threelayerarchitecture.dao.rest.dto.FormulaOneAdDto;
import com.gebel.threelayerarchitecture.dao.rest.dto.SportAdDto;
import com.gebel.threelayerarchitecture.dao.rest.interfaces.FormulaOneAdRestWs;
import com.gebel.threelayerarchitecture.dao.rest.interfaces.SportAdRestWs;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {
	
	@Mock	
	private FormulaOneAdRestWs formulaOneAdRestWs;
	
	@Mock
	private SportAdRestWs sportAdRestWs;
	
	private AdServiceImpl adService;
	
	@BeforeEach
	void setup() {
		adService = new AdServiceImpl(formulaOneAdRestWs, sportAdRestWs, new DomainAdConverter());
	}
	
	@Test
	void givenSeveralAds_whenGetPersonalizedAds_thenAllAdsRetrieved() {
		// Given
		String driverId = "test_driver_id";
		
		FormulaOneAdDto formulaOneDtoAd1 = FormulaOneAdDto.builder()
			.id("test_id1")
			.message("Formula one message 1")
			.build();
		FormulaOneAdDto formulaOneDtoAd2 = FormulaOneAdDto.builder()
			.id("test_id2")
			.message("Formula one message 2")
			.build();
		List<FormulaOneAdDto> formulaOneDtoAds = List.of(formulaOneDtoAd1, formulaOneDtoAd2);
		when(formulaOneAdRestWs.getPersonalizedAds(driverId))
			.thenReturn(formulaOneDtoAds);
		
		SportAdDto sportDtoAd1 = SportAdDto.builder()
			.id("test_id3")
			.message("Sport message 1")
			.build();
		SportAdDto sportDtoAd2 = SportAdDto.builder()
			.id("test_id4")
			.message("Sport message 2")
			.build();
		List<SportAdDto> sportDtoAds = List.of(sportDtoAd1, sportDtoAd2);
		when(sportAdRestWs.getPersonalizedAds(driverId))
			.thenReturn(sportDtoAds);
		
		// When
		List<Ad> domainAds = adService.getPersonalizedAds(driverId);
		
		// Then
		assertEquals(4, domainAds.size());
		
		Ad domainAd1 = domainAds.get(0);
		assertEquals("formulaone-test_id1", domainAd1.getId());
		assertEquals(AdCategory.FORMULA_ONE, domainAd1.getCategory());
		assertEquals("Formula one message 1", domainAd1.getMessage());
		
		Ad domainAd2 = domainAds.get(1);
		assertEquals("formulaone-test_id2", domainAd2.getId());
		assertEquals(AdCategory.FORMULA_ONE, domainAd2.getCategory());
		assertEquals("Formula one message 2", domainAd2.getMessage());
		
		Ad domainAd3 = domainAds.get(2);
		assertEquals("sport-test_id3", domainAd3.getId());
		assertEquals(AdCategory.SPORT, domainAd3.getCategory());
		assertEquals("Sport message 1", domainAd3.getMessage());
		
		Ad domainAd4 = domainAds.get(3);
		assertEquals("sport-test_id4", domainAd4.getId());
		assertEquals(AdCategory.SPORT, domainAd4.getCategory());
		assertEquals("Sport message 2", domainAd4.getMessage());
	}
	
	@Test
	void givenErrorWhenRetrievingSportAds_whenGetPersonalizedAds_thenOtherAdsRetrieved() {
		// Given
		String driverId = "test_driver_id";
		
		FormulaOneAdDto formulaOneDtoAd1 = FormulaOneAdDto.builder()
			.id("test_id1")
			.message("Formula one message 1")
			.build();
		FormulaOneAdDto formulaOneDtoAd2 = FormulaOneAdDto.builder()
			.id("test_id2")
			.message("Formula one message 2")
			.build();
		List<FormulaOneAdDto> formulaOneDtoAds = List.of(formulaOneDtoAd1, formulaOneDtoAd2);
		when(formulaOneAdRestWs.getPersonalizedAds(driverId))
			.thenReturn(formulaOneDtoAds);
		
		when(sportAdRestWs.getPersonalizedAds(driverId))
			.thenThrow(new RuntimeException("Test"));
		
		// When
		List<Ad> domainAds = adService.getPersonalizedAds(driverId);
		
		// Then
		assertEquals(2, domainAds.size());
		
		Ad domainAd1 = domainAds.get(0);
		assertEquals("formulaone-test_id1", domainAd1.getId());
		assertEquals(AdCategory.FORMULA_ONE, domainAd1.getCategory());
		assertEquals("Formula one message 1", domainAd1.getMessage());
		
		Ad domainAd2 = domainAds.get(1);
		assertEquals("formulaone-test_id2", domainAd2.getId());
		assertEquals(AdCategory.FORMULA_ONE, domainAd2.getCategory());
		assertEquals("Formula one message 2", domainAd2.getMessage());
	}
	
	@Test
	void givenErrorWhenRetrievingFormulaOneAds_whenGetPersonalizedAds_thenOtherAdsRetrieved() {
		// Given
		String driverId = "test_driver_id";
		
		when(formulaOneAdRestWs.getPersonalizedAds(driverId))
			.thenThrow(new RuntimeException("Test"));
		
		SportAdDto sportDtoAd1 = SportAdDto.builder()
			.id("test_id1")
			.message("Sport message 1")
			.build();
		SportAdDto sportDtoAd2 = SportAdDto.builder()
			.id("test_id2")
			.message("Sport message 2")
			.build();
		List<SportAdDto> sportDtoAds = List.of(sportDtoAd1, sportDtoAd2);
		when(sportAdRestWs.getPersonalizedAds(driverId))
			.thenReturn(sportDtoAds);
		
		// When
		List<Ad> domainAds = adService.getPersonalizedAds(driverId);
		
		// Then
		assertEquals(2, domainAds.size());
		
		Ad domainAd1 = domainAds.get(0);
		assertEquals("sport-test_id1", domainAd1.getId());
		assertEquals(AdCategory.SPORT, domainAd1.getCategory());
		assertEquals("Sport message 1", domainAd1.getMessage());
		
		Ad domainAd2 = domainAds.get(1);
		assertEquals("sport-test_id2", domainAd2.getId());
		assertEquals(AdCategory.SPORT, domainAd2.getCategory());
		assertEquals("Sport message 2", domainAd2.getMessage());
	}
	
	@Test
	void givenErrorWhenRetrievingSportAndFormulaOneAds_whenGetPersonalizedAds_thenEmptyListReturned() {
		// Given
		String driverId = "test_driver_id";
		
		when(formulaOneAdRestWs.getPersonalizedAds(driverId))
			.thenThrow(new RuntimeException("Test"));
		
		when(sportAdRestWs.getPersonalizedAds(driverId))
			.thenThrow(new RuntimeException("Test"));
		
		// When
		List<Ad> domainAds = adService.getPersonalizedAds(driverId);
		
		// Then
		assertTrue(domainAds.isEmpty());
	}
	
	@Test
	void givenValidDriverId_whenUnsubscribePersonalizedAds_thenUnsubscribeFromBothFormulaOneAndSportAds() {
		// Given
		String driverId = "test_driver_id";
		
		// When
		adService.unsubscribePersonalizedAds(driverId);
		
		// Then
		verify(formulaOneAdRestWs, times(1))
			.unsubscribePersonalizedAds("test_driver_id");
		verify(sportAdRestWs, times(1))
			.unsubscribePersonalizedAds("test_driver_id");
	}
	
	@Test
	void givenErrorWhenUnsubscribingFromFormulaOneAds_whenUnsubscribePersonalizedAds_thenError() {
		// Given
		String driverId = "test_driver_id";
		
		doThrow(new RuntimeException("Test"))
			.when(formulaOneAdRestWs)
			.unsubscribePersonalizedAds(driverId);
		
		// Then
		assertThrows(ExecutionException.class,() -> {
			// When
			adService.unsubscribePersonalizedAds(driverId);
		});
	}
	
	@Test
	void givenErrorWhenUnsubscribingFromSportAds_whenUnsubscribePersonalizedAds_thenError() {
		// Given
		String driverId = "test_driver_id";
		
		doThrow(new RuntimeException("Test"))
			.when(sportAdRestWs)
			.unsubscribePersonalizedAds(driverId);
		
		// Then
		assertThrows(ExecutionException.class,() -> {
			// When
			adService.unsubscribePersonalizedAds(driverId);
		});
	}
	
}