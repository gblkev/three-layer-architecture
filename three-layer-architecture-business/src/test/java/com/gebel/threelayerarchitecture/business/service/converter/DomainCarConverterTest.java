package com.gebel.threelayerarchitecture.business.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gebel.threelayerarchitecture.business.domain.Car;
import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.dao.mysql.entity.CarEntity;
import com.gebel.threelayerarchitecture.dao.mysql.entity.ColorEntity;
import com.gebel.threelayerarchitecture.dao.mysql.entity.DriverEntity;

class DomainCarConverterTest {
	
	private final DomainColorConverter domainColorConverter = new DomainColorConverter();
	private final DomainDriverConverter domainDriverConverter = new DomainDriverConverter();
	private final DomainCarConverter domainCarConverter = new DomainCarConverter(domainColorConverter, domainDriverConverter);
	
	@Test
	void givenOneCarEntity_whenToDomain_thenValidCarDomain() {
		// Given
		ColorEntity entityColor = new ColorEntity("test_color_id", "#000000");
		DriverEntity entityDriver = new DriverEntity("test_driver_id", "Forrest", "Gump");
		CarEntity entityCar = new CarEntity("test_car_id", entityColor, entityDriver);
		
		// When
		Car domainCar = domainCarConverter.toDomain(entityCar);
		
		// Then
		assertEquals("test_car_id", domainCar.getId());
		
		Color domainColor = domainCar.getColor();
		assertEquals("test_color_id", domainColor.getId());
		assertEquals("#000000", domainColor.getHexaCode());
		
		Driver domainDriver = domainCar.getDriver();
		assertEquals("test_driver_id", domainDriver.getId());
		assertEquals("Forrest", domainDriver.getFirstName());
		assertEquals("Gump", domainDriver.getLastName());
	}
	
	@Test
	void givenNullCarEntity_whenToDomain_thenNullCarDomain() {
		// Given
		CarEntity entityCar = null;
		
		// When
		Car domainCar = domainCarConverter.toDomain(entityCar);
		
		// Then
		assertNull(domainCar);
	}
	
	@Test
	void givenSeveralCarEntities_whenToDomain_thenValidCarDomains() {
		// Given
		ColorEntity entityColor1 = new ColorEntity("test_color_id1", "#000000");
		DriverEntity entityDriver1 = new DriverEntity("test_driver_id1", "Forrest", "Gump");
		CarEntity entityCar1 = new CarEntity("test_car_id1", entityColor1, entityDriver1);
		
		ColorEntity entityColor2 = new ColorEntity("test_color_id2", "#000001");
		DriverEntity entityDriver2 = new DriverEntity("test_driver_id2", "Tom", "Hanks");
		CarEntity entityCar2 = new CarEntity("test_car_id2", entityColor2, entityDriver2);
		
		List<CarEntity> entitiesCars = List.of(entityCar1, entityCar2);
		
		// When
		List<Car> domainCars = domainCarConverter.toDomain(entitiesCars);
		
		// Then
		assertEquals(2, domainCars.size());
		
		Car domainCar1 = domainCars.get(0);
		assertEquals("test_car_id1", domainCar1.getId());
		Color domainColor1 = domainCar1.getColor();
		assertEquals("test_color_id1", domainColor1.getId());
		assertEquals("#000000", domainColor1.getHexaCode());
		Driver domainDriver1 = domainCar1.getDriver();
		assertEquals("test_driver_id1", domainDriver1.getId());
		assertEquals("Forrest", domainDriver1.getFirstName());
		assertEquals("Gump", domainDriver1.getLastName());
		
		Car domainCar2 = domainCars.get(1);
		assertEquals("test_car_id2", domainCar2.getId());
		Color domainColor2 = domainCar2.getColor();
		assertEquals("test_color_id2", domainColor2.getId());
		assertEquals("#000001", domainColor2.getHexaCode());
		Driver domainDriver2 = domainCar2.getDriver();
		assertEquals("test_driver_id2", domainDriver2.getId());
		assertEquals("Tom", domainDriver2.getFirstName());
		assertEquals("Hanks", domainDriver2.getLastName());
	}
	
	@Test
	void givenOneDomainCar_whenToEntity_thenValidCarEntity() {
		// Given
		Color domainColor = new Color("test_color_id", "#000000");
		Driver domainDriver = new Driver("test_driver_id", "Forrest", "Gump");
		Car domainCar = new Car("test_car_id", domainColor, domainDriver);
		
		// When
		CarEntity entityCar = domainCarConverter.toEntity(domainCar);
		
		// Then
		assertEquals("test_car_id", entityCar.getId());
		
		ColorEntity entityColor = entityCar.getColor();
		assertEquals("test_color_id", entityColor.getId());
		assertEquals("#000000", entityColor.getHexaCode());
		
		DriverEntity entityDriver = entityCar.getDriver();
		assertEquals("test_driver_id", entityDriver.getId());
		assertEquals("Forrest", entityDriver.getFirstName());
		assertEquals("Gump", entityDriver.getLastName());
	}
	
	@Test
	void givenNullDomainCar_whenToEntity_thenNullCarEntity() {
		// Given
		Car domainCar = null;
		
		// When
		CarEntity entityCar = domainCarConverter.toEntity(domainCar);
		
		// Then
		assertNull(entityCar);
	}

}