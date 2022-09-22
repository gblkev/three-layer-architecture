package com.gebel.threelayerarchitecture.controller.api.v2.interfaces;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.CreateDriverDto;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.DriverDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V2ApiBaseUri.API_V2_BASE_URI + "/drivers")
@Tag(name = "Drivers")
public interface DriverV2Endpoint {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "List all the drivers")
	List<DriverDto> getAllDrivers();
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a new driver")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "ApiBusinessErrorCodeDto.DRIVER_INVALID_FIRST_NAME"),
		@ApiResponse(responseCode = "ApiBusinessErrorCodeDto.DRIVER_INVALID_LAST_NAME")
	})
	DriverDto createDriver(@RequestBody @Schema(required = true, implementation = CreateDriverDto.class) CreateDriverDto createDriverDto) throws BusinessException;
	
	@DeleteMapping("/{driverId}")
	@Operation(summary = "Delete an existing driver")
	void deleteDriver(@PathVariable("driverId") String driverId);
	
}