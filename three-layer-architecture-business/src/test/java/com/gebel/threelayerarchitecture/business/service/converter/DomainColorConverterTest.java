package com.gebel.threelayerarchitecture.business.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.dao.mysql.entity.ColorEntity;

class DomainColorConverterTest {

	private final DomainColorConverter domainColorConverter = new DomainColorConverter();
	
	@Test
	void givenOneColorEntity_whenToDomain_thenValidColorDomain() {
		// Given
		ColorEntity entityColor = ColorEntity.builder()
			.id("test_id")
			.hexaCode("#000000")
			.build();
		
		// When
		Color domainColor = domainColorConverter.toDomain(entityColor);
		
		// Then
		assertEquals("test_id", domainColor.getId());
		assertEquals("#000000", domainColor.getHexaCode());
	}
	
	@Test
	void givenNullColorEntity_whenToDomain_thenNullColorDomain() {
		// Given
		ColorEntity entityColor = null;
		
		// When
		Color domainColor = domainColorConverter.toDomain(entityColor);
		
		// Then
		assertNull(domainColor);
	}
	
	@Test
	void givenSeveralColorEntities_whenToDomain_thenValidColorDomains() {
		// Given
		ColorEntity entityColor1 = ColorEntity.builder()
			.id("test_id1")
			.hexaCode("#000001")
			.build();
		ColorEntity entityColor2 = ColorEntity.builder()
			.id("test_id2")
			.hexaCode("#000002")
			.build();
		List<ColorEntity> entitiesColors = List.of(entityColor1, entityColor2);
		
		// When
		List<Color> domainColors = domainColorConverter.toDomain(entitiesColors);
		
		// Then
		assertEquals(2, domainColors.size());
		
		Color domainColor1 = domainColors.get(0);
		assertEquals("test_id1", domainColor1.getId());
		assertEquals("#000001", domainColor1.getHexaCode());
		
		Color domainColor2 = domainColors.get(1);
		assertEquals("test_id2", domainColor2.getId());
		assertEquals("#000002", domainColor2.getHexaCode());
	}
	
	@Test
	void givenOneDomainColor_whenToEntity_thenValidColorEntity() {
		// Given
		Color domainColor = Color.builder()
			.id("test_id")
			.hexaCode("#000000")
			.build();
		
		// When
		ColorEntity entityColor = domainColorConverter.toEntity(domainColor);
		
		// Then
		assertEquals("test_id", entityColor.getId());
		assertEquals("#000000", entityColor.getHexaCode());
	}
	
	@Test
	void givenNullDomainColor_whenToEntity_thenNullColorEntity() {
		// Given
		Color domainColor = null;
		
		// When
		ColorEntity entityColor = domainColorConverter.toEntity(domainColor);
		
		// Then
		assertNull(entityColor);
	}

}