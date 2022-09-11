package com.gebel.threelayerarchitecture.controller.api.v1.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiDriverConverter;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.CreateDriverDto;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.DriverDto;
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
		LOGGER.info("Creating driver with firstName={}", hexaCode);
		return colorConverter.toDto(colorService.createColor(hexaCode));
	}
	
	private String replaceAllCharactersWithWildcardExceptFirstLetter(String s) {
		if (StringUtils.length(s) )
		return s.charAt(0) + s.substring(1).replaceAll("*", "*");
	}

	@Override
	public void deleteDriver(String driverId) {
		LOGGER.info("Deleting driver with id={}", driverId);
		driverService.deleteDriver(driverId);
	}

}