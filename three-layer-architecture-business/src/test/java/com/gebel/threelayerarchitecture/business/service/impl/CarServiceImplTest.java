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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gebel.threelayerarchitecture.business.domain.BusinessErrorCode;
import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Car;
import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.business.service.converter.DomainCarConverter;
import com.gebel.threelayerarchitecture.business.service.converter.DomainColorConverter;
import com.gebel.threelayerarchitecture.business.service.converter.DomainDriverConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.dao.mysql.entity.CarEntity;
import com.gebel.threelayerarchitecture.dao.mysql.entity.ColorEntity;
import com.gebel.threelayerarchitecture.dao.mysql.entity.DriverEntity;
import com.gebel.threelayerarchitecture.dao.mysql.interfaces.CarRepository;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
	
	@Mock
	private CarRepository carRepository;
	
	@Mock
	private ColorService colorService;
	
	@Mock
	private DriverService driverService;
	
	private CarServiceImpl carService;
	
	
	@BeforeEach
	void setup() {
		DomainCarConverter domainCarConverter = new DomainCarConverter(new DomainColorConverter(), new DomainDriverConverter());
		carService = new CarServiceImpl(carRepository, domainCarConverter, colorService, driverService);
	}
	
	@Test
	void givenSeveralCars_whenGetAllCars_thenAllCarsRetrieved() {
		// Given
		ColorEntity entityColor1 = new ColorEntity("test_color_id1", "#000000");
		DriverEntity entityDriver1 = new DriverEntity("test_driver_id1", "Forrest", "Gump");
		CarEntity entityCar1 = new CarEntity("test_car_id1", entityColor1, entityDriver1);
		
		ColorEntity entityColor2 = new ColorEntity("test_color_id2", "#000001");
		DriverEntity entityDriver2 = new DriverEntity("test_driver_id2", "Tom", "Hanks");
		CarEntity entityCar2 = new CarEntity("test_car_id2", entityColor2, entityDriver2);
		
		List<CarEntity> entitiesCars = List.of(entityCar1, entityCar2);
		when(carRepository.findAll())
			.thenReturn(entitiesCars);

		// When
		List<Car> foundDomainCars = carService.getAllCars();
		
		// Then
		assertEquals(2, foundDomainCars.size());
		
		Car foundDomainCar1 = foundDomainCars.get(0);
		assertEquals("test_car_id1", foundDomainCar1.getId());
		Color foundDomainColor1 = foundDomainCar1.getColor();
		assertEquals("test_color_id1", foundDomainColor1.getId());
		assertEquals("#000000", foundDomainColor1.getHexaCode());
		Driver foundDomainDriver1 = foundDomainCar1.getDriver();
		assertEquals("test_driver_id1", foundDomainDriver1.getId());
		assertEquals("Forrest", foundDomainDriver1.getFirstName());
		assertEquals("Gump", foundDomainDriver1.getLastName());
		
		Car foundDomainCar2 = foundDomainCars.get(1);
		assertEquals("test_car_id2", foundDomainCar2.getId());
		Color foundDomainColor2 = foundDomainCar2.getColor();
		assertEquals("test_color_id2", foundDomainColor2.getId());
		assertEquals("#000001", foundDomainColor2.getHexaCode());
		Driver foundDomainDriver2 = foundDomainCar2.getDriver();
		assertEquals("test_driver_id2", foundDomainDriver2.getId());
		assertEquals("Tom", foundDomainDriver2.getFirstName());
		assertEquals("Hanks", foundDomainDriver2.getLastName());
	}
	
	@Test
	void givenSeveralCars_whenCountCars_thenRightCount() {
		// Given
		when(carRepository.count())
			.thenReturn(128L);
		
		// When
		long count = carService.countCars();
		
		// Then
		assertEquals(128, count);
	}
	
	@Test
	void givenValidCar_whenCreateCar_thenCarCreated() throws BusinessException {
		// Given
		ColorEntity createdCarEntityColor = new ColorEntity("test_color_id", "#000000");
		DriverEntity createdCarEntityDriver = new DriverEntity("test_driver_id", "Forrest", "Gump");
		CarEntity createdEntityCar = new CarEntity("test_car_id", createdCarEntityColor, createdCarEntityDriver);
		
		ArgumentCaptor<CarEntity> carEntityArgumentCaptor = ArgumentCaptor.forClass(CarEntity.class);
		when(carRepository.save(carEntityArgumentCaptor.capture()))
			.thenReturn(createdEntityCar);
		
		Color getColorByIdMock = new Color("test_color_id", "#000000");
		when(colorService.getColorById("test_color_id"))
			.thenReturn(getColorByIdMock);
		
		Driver getDriverByIdMock = new Driver("test_driver_id", "Forrest", "Gump");
		when(driverService.getDriverById("test_driver_id"))
			.thenReturn(getDriverByIdMock);
		
		String colorIdOfCarToCreate = "test_color_id";
		String driverIdOfCarToCreate = "test_driver_id";
		
		// When
		Car createdDomainCar = carService.createCar(colorIdOfCarToCreate, driverIdOfCarToCreate);
		
		// Then
		CarEntity carEntityPassedToDao = carEntityArgumentCaptor.getValue();
		assertNull(carEntityPassedToDao.getId());
		ColorEntity colorEntityPassedToDao = carEntityPassedToDao.getColor();
		assertEquals("test_color_id", colorEntityPassedToDao.getId());
		assertEquals("#000000", colorEntityPassedToDao.getHexaCode());
		DriverEntity driverEntityPassedToDao = carEntityPassedToDao.getDriver();
		assertEquals("test_driver_id", driverEntityPassedToDao.getId());
		assertEquals("Forrest", driverEntityPassedToDao.getFirstName());
		assertEquals("Gump", driverEntityPassedToDao.getLastName());
		
		assertEquals("test_car_id", createdDomainCar.getId());
		Color createdCarDomainColor = createdDomainCar.getColor();
		assertEquals("test_color_id", createdCarDomainColor.getId());
		assertEquals("#000000", createdCarDomainColor.getHexaCode());
		Driver createdCarDomainDriver = createdDomainCar.getDriver();
		assertEquals("test_driver_id", createdCarDomainDriver.getId());
		assertEquals("Forrest", createdCarDomainDriver.getFirstName());
		assertEquals("Gump", createdCarDomainDriver.getLastName());
	}
	
	@Test
	void givenInvalidColor_whenCreateCar_thenThrowBusinessException() throws BusinessException {
		// Given
		String invalidColorId = "test_color_id";
		String validDriverId = "test_driver_id";
		
		when(colorService.getColorById(invalidColorId))
			.thenReturn(null);
		
		Driver getDriverByIdMock = new Driver(validDriverId, "Forrest", "Gump");
		when(driverService.getDriverById(validDriverId))
			.thenReturn(getDriverByIdMock);
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			carService.createCar(invalidColorId, validDriverId);
		});

		// Then
		verifyNoInteractions(carRepository);
		assertEquals(BusinessErrorCode.CAR_INVALID_COLOR, businessException.getBusinessError());
	}
	
	@Test
	void givenInvalidDriver_whenCreateCar_thenThrowBusinessException() throws BusinessException {
		// Given
		String validColorId = "test_color_id";
		String invalidDriverId = "test_driver_id";
		
		Color getColorByIdMock = new Color(validColorId, "#000000");
		when(colorService.getColorById(validColorId))
			.thenReturn(getColorByIdMock);
		
		when(driverService.getDriverById(invalidDriverId))
			.thenReturn(null);
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			carService.createCar(validColorId, invalidDriverId);
		});

		// Then
		verifyNoInteractions(carRepository);
		assertEquals(BusinessErrorCode.CAR_INVALID_DRIVER, businessException.getBusinessError());
	}
	
	@Test
	void givenExistingCar_whenDeleteCar_thenCarDeleted() {
		// Given
		String existingCarId = "existing_id";
		when(carRepository.existsById("existing_id"))
			.thenReturn(true);
		
		// When
		carService.deleteCar(existingCarId);
		
		// Then
		verify(carRepository, times(1))
			.deleteById("existing_id");
	}
	
	@Test
	void givenNonExistingCar_whenDeleteCar_thenDeleteNotDone() {
		// Given
		String nonExistingCarId = "non_existing_id";
		when(carRepository.existsById("non_existing_id"))
			.thenReturn(false);
		
		// When
		carService.deleteCar(nonExistingCarId);
		
		// Then
		verify(carRepository,never())
			.deleteById(any());
	}
	
}