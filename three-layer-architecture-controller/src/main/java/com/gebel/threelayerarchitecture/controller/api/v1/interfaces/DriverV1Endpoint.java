package com.gebel.threelayerarchitecture.controller.api.v1.interfaces;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.v1.dto.CreateDriverDto;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.DriverDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V1ApiBaseUri.API_V1_BASE_URI + "/drivers")
@Tag(name = "Drivers")
public interface DriverV1Endpoint {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "List all the drivers")
	List<DriverDto> getAllDrivers();
	
	@PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a new driver")
	DriverDto createDriver(@RequestBody @Schema(required = true) CreateDriverDto createDriverDto);
	
	@DeleteMapping("/{driverId}")
	@Operation(summary = "Delete an existing driver")
	void deleteDriver(@PathVariable("driverId") String driverId);
	
}