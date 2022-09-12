package com.gebel.threelayerarchitecture.controller.api.v1.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Car;
import com.gebel.threelayerarchitecture.business.service.interfaces.CarService;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiCarConverter;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.CarDto;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.CreateCarDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessException;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.CarV1Endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class CarV1EndpointImpl implements CarV1Endpoint {

	private final CarService carService;
	private final V1ApiCarConverter carConverter;
	
	@Override
	public List<CarDto> getAllCars() {
		LOGGER.info("Listing all cars");
		return carConverter.toDto(carService.getAllCars());
	}

	@Override
	public CarDto createCar(CreateCarDto createCarDto) {
		try {
			LOGGER.info("Creating car with data={}", createCarDto);
			String colorId = (createCarDto != null ? createCarDto.getColorId() : null);
			String driverId = (createCarDto != null ? createCarDto.getDriverId() : null);
			Car createdCar = carService.createCar(colorId, driverId);
			return carConverter.toDto(createdCar);
		}
		catch (BusinessException businessException) {
			throw new ApiBusinessException(businessException);
		}
	}

	@Override
	public void deleteCar(String carId) {
		LOGGER.info("Deleting car with id={}", carId);
		carService.deleteCar(carId);
	}

}