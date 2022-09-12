package com.gebel.threelayerarchitecture.business.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.dao.db.entity.DriverEntity;

class DomainDriverConverterTest {

	private final DomainDriverConverter domainDriverConverter = new DomainDriverConverter();
	
	@Test
	void givenOneDriverEntity_whenToDomain_thenValidDriverDomain() {
		// Given
		DriverEntity entityDriver = DriverEntity.builder()
			.id("test_id")
			.firstName("Forrest")
			.lastName("Gump")
			.build();
		
		// When
		Driver domainDriver = domainDriverConverter.toDomain(entityDriver);
		
		// Then
		assertEquals("test_id", domainDriver.getId());
		assertEquals("Forrest", domainDriver.getFirstName());
		assertEquals("Gump", domainDriver.getLastName());
	}
	
	@Test
	void givenNullDriverEntity_whenToDomain_thenNullDriverDomain() {
		// Given
		DriverEntity entityDriver = null;
		
		// When
		Driver domainDriver = domainDriverConverter.toDomain(entityDriver);
		
		// Then
		assertNull(domainDriver);
	}
	
	@Test
	void givenSeveralDriverEntities_whenToDomain_thenValidDriverDomains() {
		// Given
		DriverEntity entityDriver1 = DriverEntity.builder()
			.id("test_id1")
			.firstName("Joe")
			.lastName("Biden")
			.build();
		DriverEntity entityDriver2 = DriverEntity.builder()
			.id("test_id2")
			.firstName("Donald")
			.lastName("Trump")
			.build();
		List<DriverEntity> entitiesDrivers = List.of(entityDriver1, entityDriver2);
		
		// When
		List<Driver> domainDrivers = domainDriverConverter.toDomain(entitiesDrivers);
		
		// Then
		assertEquals(2, domainDrivers.size());
		
		Driver domainDriver1 = domainDrivers.get(0);
		assertEquals("test_id1", domainDriver1.getId());
		assertEquals("Joe", domainDriver1.getFirstName());
		assertEquals("Biden", domainDriver1.getLastName());
		
		Driver domainDriver2 = domainDrivers.get(1);
		assertEquals("test_id2", domainDriver2.getId());
		assertEquals("Donald", domainDriver2.getFirstName());
		assertEquals("Trump", domainDriver2.getLastName());
	}
	
	@Test
	void givenOneDomainDriver_whenToEntity_thenValidDriverEntity() {
		// Given
		Driver domainDriver = Driver.builder()
			.id("test_id")
			.firstName("Tom")
			.lastName("Hanks")
			.build();
		
		// When
		DriverEntity entityDriver = domainDriverConverter.toEntity(domainDriver);
		
		// Then
		assertEquals("test_id", entityDriver.getId());
		assertEquals("Tom", entityDriver.getFirstName());
		assertEquals("Hanks", entityDriver.getLastName());
	}
	
	@Test
	void givenNullDomainDriver_whenToEntity_thenNullDriverEntity() {
		// Given
		Driver domainDriver = null;
		
		// When
		DriverEntity entityDriver = domainDriverConverter.toEntity(domainDriver);
		
		// Then
		assertNull(entityDriver);
	}

}