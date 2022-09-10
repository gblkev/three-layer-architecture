package com.gebel.threelayerarchitecture.controller.api.v1.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiDriverConverter;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.DriverDto;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.DriverV1Endpoint;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class DriverV1EndpointImpl implements DriverV1Endpoint {

	private final DriverService driverService;
	private final V1ApiDriverConverter driverConverter;
	
	@Override
	public List<DriverDto> getAllDrivers() {
		return driverConverter.toDto(driverService.getAllDrivers());
	}

}