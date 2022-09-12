package com.gebel.threelayerarchitecture.controller.api.v1.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiDriverConverter;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.CreateDriverDto;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.DriverDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessException;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.DriverV1Endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class DriverV1EndpointImpl implements DriverV1Endpoint {

	private final DriverService driverService;
	private final V1ApiDriverConverter driverConverter;
	
	@Override
	public List<DriverDto> getAllDrivers() {
		LOGGER.info("Listing all drivers");
		return driverConverter.toDto(driverService.getAllDrivers());
	}

	@Override
	public DriverDto createDriver(CreateDriverDto createDriverDto) {
		try {
			LOGGER.info("Creating driver with firstName='{}' and lastName='{}'",
				getCidCompliantName(createDriverDto.getFirstName()),
				getCidCompliantName(createDriverDto.getLastName()));
			Driver createdDriver = driverService.createDriver(createDriverDto.getFirstName(), createDriverDto.getLastName());
			return driverConverter.toDto(createdDriver);
		}
		catch (BusinessException businessException) {
			throw new ApiBusinessException(businessException);
		}
	}
	
	private String getCidCompliantName(String name) {
		return replaceAllCharactersWithWildcardExceptFirstLetter(name);
	}
	
	private static String replaceAllCharactersWithWildcardExceptFirstLetter(String s) {
		if (StringUtils.isEmpty(s)) {
			return s;
		}
		return s.charAt(0) + "******";
	}

	@Override
	public void deleteDriver(String driverId) {
		LOGGER.info("Deleting driver with id={}", driverId);
		driverService.deleteDriver(driverId);
	}

}