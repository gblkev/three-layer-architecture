package com.gebel.threelayerarchitecture.business.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gebel.threelayerarchitecture.business.domain.Ad;
import com.gebel.threelayerarchitecture.business.domain.AdCategory;
import com.gebel.threelayerarchitecture.dao.rest.dto.FormulaOneAdDto;
import com.gebel.threelayerarchitecture.dao.rest.dto.SportAdDto;

class DomainAdConverterTest {

	private final DomainAdConverter domainAdConverter = new DomainAdConverter();
	
	@Test
	void givenOneFormulaOneAdDto_whenFormulaOneAdToDomain_thenValidAdDomain() {
		// Given
		FormulaOneAdDto dtoAd = FormulaOneAdDto.builder()
			.id("test_id")
			.message("Test message")
			.build();
		
		// When
		Ad domainAd = domainAdConverter.formulaOneAdToDomain(dtoAd);
		
		// Then
		assertEquals("formulaone-test_id", domainAd.getId());
		assertEquals(AdCategory.FORMULA_ONE, domainAd.getCategory());
		assertEquals("Test message", domainAd.getMessage());
	}
	
	@Test
	void givenNullFormulaOneAdDto_whenFormulaOneAdToDomain_thenNullAdDomain() {
		// Given
		FormulaOneAdDto dtoAd = null;
		
		// When
		Ad domainAd = domainAdConverter.formulaOneAdToDomain(dtoAd);
		
		// Then
		assertNull(domainAd);
	}
	
	@Test
	void givenSeveralFormulaOneAdDtos_whenFormulaOneAdsToDomain_thenValidAdDomains() {
		// Given
		FormulaOneAdDto dtoAd1 = FormulaOneAdDto.builder()
			.id("test_id1")
			.message("Test message 1")
			.build();
		FormulaOneAdDto dtoAd2 = FormulaOneAdDto.builder()
			.id("test_id2")
			.message("Test message 2")
			.build();
		List<FormulaOneAdDto> dtosAds = List.of(dtoAd1, dtoAd2);
		
		// When
		List<Ad> domainAds = domainAdConverter.formulaOneAdsToDomain(dtosAds);
		
		// Then
		assertEquals(2, domainAds.size());
		
		Ad domainAd1 = domainAds.get(0);
		assertEquals("formulaone-test_id1", domainAd1.getId());
		assertEquals(AdCategory.FORMULA_ONE, domainAd1.getCategory());
		assertEquals("Test message 1", domainAd1.getMessage());
		
		Ad domainAd2 = domainAds.get(1);
		assertEquals("formulaone-test_id2", domainAd2.getId());
		assertEquals(AdCategory.FORMULA_ONE, domainAd2.getCategory());
		assertEquals("Test message 2", domainAd2.getMessage());
	}
	
	@Test
	void givenOneSportAdDto_whenSportAdToDomain_thenValidAdDomain() {
		// Given
		SportAdDto dtoAd = SportAdDto.builder()
			.id("test_id")
			.message("Test message")
			.build();
		
		// When
		Ad domainAd = domainAdConverter.sportAdToDomain(dtoAd);
		
		// Then
		assertEquals("sport-test_id", domainAd.getId());
		assertEquals(AdCategory.SPORT, domainAd.getCategory());
		assertEquals("Test message", domainAd.getMessage());
	}
	
	@Test
	void givenNullSportAdDto_whenSportAdToDomain_thenNullAdDomain() {
		// Given
		SportAdDto dtoAd = null;
		
		// When
		Ad domainAd = domainAdConverter.sportAdToDomain(dtoAd);
		
		// Then
		assertNull(domainAd);
	}
	
	@Test
	void givenSeveralSportAdDtos_whenSportAdsToDomain_thenValidAdDomains() {
		// Given
		SportAdDto dtoAd1 = SportAdDto.builder()
			.id("test_id1")
			.message("Test message 1")
			.build();
		SportAdDto dtoAd2 = SportAdDto.builder()
			.id("test_id2")
			.message("Test message 2")
			.build();
		List<SportAdDto> dtosAds = List.of(dtoAd1, dtoAd2);
		
		// When
		List<Ad> domainAds = domainAdConverter.sportAdsToDomain(dtosAds);
		
		// Then
		assertEquals(2, domainAds.size());
		
		Ad domainAd1 = domainAds.get(0);
		assertEquals("sport-test_id1", domainAd1.getId());
		assertEquals(AdCategory.SPORT, domainAd1.getCategory());
		assertEquals("Test message 1", domainAd1.getMessage());
		
		Ad domainAd2 = domainAds.get(1);
		assertEquals("sport-test_id2", domainAd2.getId());
		assertEquals(AdCategory.SPORT, domainAd2.getCategory());
		assertEquals("Test message 2", domainAd2.getMessage());
	}

}