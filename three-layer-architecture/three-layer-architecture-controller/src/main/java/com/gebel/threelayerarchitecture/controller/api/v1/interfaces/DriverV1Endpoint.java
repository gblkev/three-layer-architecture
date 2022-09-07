package com.gebel.threelayerarchitecture.controller.api.v1.interfaces;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.v1.dto.DriverDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V1ApiBaseUri.API_V1_BASE_URI + "/drivers")
@Tag(name = "Drivers")
public interface DriverV1Endpoint {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "List all the drivers")
	List<DriverDto> getAllDrivers();
	
}