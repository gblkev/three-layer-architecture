package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gebel.threelayerarchitecture.business.domain.BusinessErrorCode;
import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Car;
import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.business.service.converter.DomainCarConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.CarService;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.dao.db.entity.CarEntity;
import com.gebel.threelayerarchitecture.dao.db.interfaces.CarRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

	private final CarRepository carRepository;
	private final DomainCarConverter carConverter;
	private final ColorService colorService;
	private final DriverService driverService;
	
	@Override
	@Transactional(readOnly = true)
	public List<Car> getAllCars() {
		return carConverter.toDomain(carRepository.findAll());
	}

	@Override
	@Transactional
	public Car createCar(String colorId, String driverId) throws BusinessException {
		Color color = colorService.getColorById(colorId);
		Driver driver = driverService.getDriverById(driverId);
		Car car = Car.builder()
			.color(color)
			.driver(driver)
			.build();
		checkCarCreationPrerequisites(car);
		return createCarInDb(car);
	}
	
	private void checkCarCreationPrerequisites(Car car) throws BusinessException {
		if (car.getColor() == null) {
			throw new BusinessException(BusinessErrorCode.CAR_INVALID_COLOR);
		}
		if (car.getDriver() == null) {
			throw new BusinessException(BusinessErrorCode.CAR_INVALID_DRIVER);
		}
	}
	
	private Car createCarInDb(Car car) {
		CarEntity carEntity = carConverter.toEntity(car);
		carEntity = carRepository.save(carEntity);
		return carConverter.toDomain(carEntity);
	}

	@Override
	@Transactional
	public void deleteCar(String carId) {
		if (carRepository.existsById(carId)) {
			carRepository.deleteById(carId);
		}
	}
	
}