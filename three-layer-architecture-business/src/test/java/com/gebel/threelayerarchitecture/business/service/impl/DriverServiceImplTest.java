package com.gebel.threelayerarchitecture.business.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gebel.threelayerarchitecture.business.domain.BusinessErrorCode;
import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.business.service.converter.DomainDriverConverter;
import com.gebel.threelayerarchitecture.dao.mysql.entity.DriverEntity;
import com.gebel.threelayerarchitecture.dao.mysql.interfaces.DriverRepository;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {
	
	@Mock
	private DriverRepository driverRepository;
	
	private DriverServiceImpl driverService;
	
	@BeforeEach
	void setup() {
		driverService = new DriverServiceImpl(driverRepository, new DomainDriverConverter());
	}
	
	@Test
	void givenExistingDriver_whenGetById_thenDriverRetrieved() {
		// Given
		String driverId = "test_id1";
		DriverEntity entityDriver = DriverEntity.builder()
			.id(driverId)
			.firstName("Forrest")
			.lastName("Gump")
			.build();
		when(driverRepository.findById(driverId))
			.thenReturn(Optional.of(entityDriver));
		
		// When
		Driver domainDriver = driverService.getDriverById(driverId);
		
		// Then
		assertEquals("test_id1", domainDriver.getId());
		assertEquals("Forrest", domainDriver.getFirstName());
		assertEquals("Gump", domainDriver.getLastName());
	}
	
	@Test
	void givenNonExistingDriver_whenGetById_thenNullReturned() {
		// Given
		String driverId = "test_id1";
		when(driverRepository.findById(driverId))
			.thenReturn(Optional.empty());
		
		// When
		Driver domainDriver = driverService.getDriverById(driverId);
		
		// Then
		assertNull(domainDriver);
	}

	@Test
	void givenSeveralDrivers_whenGetAllDrivers_thenAllDriversRetrieved() {
		// Given
		DriverEntity entityDriver1 = DriverEntity.builder()
			.id("test_id1")
			.firstName("Forrest")
			.lastName("Gump")
			.build();
		DriverEntity entityDriver2 = DriverEntity.builder()
			.id("test_id2")
			.firstName("Tom")
			.lastName("Hanks")
			.build();
		List<DriverEntity> entitiesDrivers = List.of(entityDriver1, entityDriver2);
		when(driverRepository.findAll())
			.thenReturn(entitiesDrivers);
		
		// When
		List<Driver> domainDrivers = driverService.getAllDrivers();
		
		// Then
		assertEquals(2, domainDrivers.size());
		
		Driver domainDriver1 = domainDrivers.get(0);
		assertEquals("test_id1", domainDriver1.getId());
		assertEquals("Forrest", domainDriver1.getFirstName());
		assertEquals("Gump", domainDriver1.getLastName());
		
		Driver domainDriver2 = domainDrivers.get(1);
		assertEquals("test_id2", domainDriver2.getId());
		assertEquals("Tom", domainDriver2.getFirstName());
		assertEquals("Hanks", domainDriver2.getLastName());
	}
	
	@Test
	void givenSeveralDrivers_whenCountDrivers_thenRightCount() {
		// Given
		when(driverRepository.count())
			.thenReturn(13L);
		
		// When
		long count = driverService.countDrivers();
		
		// Then
		assertEquals(13, count);
	}
	
	@Test
	void givenValidDriver_whenCreateDriver_thenDriverCreated() throws BusinessException {
		// Given
		DriverEntity createdEntityDriver = DriverEntity.builder()
			.id("test_id")
			.firstName("Forrest")
			.lastName("Gump")
			.build();
		ArgumentCaptor<DriverEntity> driverEntityArgumentCaptor = ArgumentCaptor.forClass(DriverEntity.class);
		when(driverRepository.save(driverEntityArgumentCaptor.capture()))
			.thenReturn(createdEntityDriver);
		
		String firstNameOfDriverToCreate = "Forrest";
		String lastNameOfDriverToCreate = "Gump";
		
		// When
		Driver createdDomainDriver = driverService.createDriver(firstNameOfDriverToCreate, lastNameOfDriverToCreate);
		
		// Then
		assertNull(driverEntityArgumentCaptor.getValue().getId());
		assertEquals("Forrest", driverEntityArgumentCaptor.getValue().getFirstName());
		assertEquals("Gump", driverEntityArgumentCaptor.getValue().getLastName());
		
		assertEquals("test_id", createdDomainDriver.getId());
		assertEquals("Forrest", createdDomainDriver.getFirstName());
		assertEquals("Gump", createdDomainDriver.getLastName());
	}
	
	@Test
	void givenInvalidFirstName_whenCreateDriver_thenThrowBusinessException() throws BusinessException {
		// Given
		String firstNameOfDriverToCreate = null;
		String lastNameOfDriverToCreate = "Gump";
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			driverService.createDriver(firstNameOfDriverToCreate, lastNameOfDriverToCreate);
		});

		// Then
		verifyNoInteractions(driverRepository);
		assertEquals(BusinessErrorCode.DRIVER_INVALID_FIRST_NAME, businessException.getBusinessError());
	}
	
	@Test
	void givenInvalidLastName_whenCreateDriver_thenThrowBusinessException() throws BusinessException {
		// Given
		String firstNameOfDriverToCreate = "Forrest";
		String lastNameOfDriverToCreate = null;
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			driverService.createDriver(firstNameOfDriverToCreate, lastNameOfDriverToCreate);
		});

		// Then
		verifyNoInteractions(driverRepository);
		assertEquals(BusinessErrorCode.DRIVER_INVALID_LAST_NAME, businessException.getBusinessError());
	}
	
	@Test
	void givenExistingDriver_whenDeleteDriver_thenDriverDeleted() {
		// Given
		String existingDriverId = "existing_id";
		when(driverRepository.existsById("existing_id"))
			.thenReturn(true);
		
		// When
		driverService.deleteDriver(existingDriverId);
		
		// Then
		verify(driverRepository, times(1))
			.deleteById("existing_id");
	}
	
	@Test
	void givenNonExistingDriver_whenDeleteDriver_thenDeleteNotDone() {
		// Given
		String nonExistingDriverId = "non_existing_id";
		when(driverRepository.existsById("non_existing_id"))
			.thenReturn(false);
		
		// When
		driverService.deleteDriver(nonExistingDriverId);
		
		// Then
		verify(driverRepository,never())
			.deleteById(any());
	}
	
}