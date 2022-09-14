package com.gebel.threelayerarchitecture.controller.api.v2.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.controller.api.v2.converter.V2ApiDriverConverter;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.CreateDriverDto;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.DriverDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.ApiBusinessException;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.DriverV2Endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class DriverV2EndpointImpl implements DriverV2Endpoint {

	private final DriverService driverService;
	private final V2ApiDriverConverter driverConverter;
	
	@Override
	public List<DriverDto> getAllDrivers() {
		LOGGER.info("Listing all drivers");
		return driverConverter.toDto(driverService.getAllDrivers());
	}

	@Override
	public DriverDto createDriver(CreateDriverDto createDriverDto) {
		try {
			LOGGER.info("Creating driver with data={}", createDriverDto);
			String firstName = (createDriverDto != null ? createDriverDto.getFirstName() : null);
			String lastName = (createDriverDto != null ? createDriverDto.getLastName() : null);
			Driver createdDriver = driverService.createDriver(firstName, lastName);
			return driverConverter.toDto(createdDriver);
		}
		catch (BusinessException businessException) {
			throw new ApiBusinessException(businessException);
		}
	}
	
	@Override
	public void deleteDriver(String driverId) {
		LOGGER.info("Deleting driver with id={}", driverId);
		driverService.deleteDriver(driverId);
	}

}