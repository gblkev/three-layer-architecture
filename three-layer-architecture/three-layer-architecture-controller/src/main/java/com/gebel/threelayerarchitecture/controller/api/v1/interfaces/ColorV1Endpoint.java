package com.gebel.threelayerarchitecture.controller.api.v1.interfaces;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.v1.dto.ColorDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V1ApiBaseUri.API_V1_BASE_URI + "/colors")
@Tag(name = "Colors")
public interface ColorV1Endpoint {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "List all the available colors")
	List<ColorDto> getAllAvailableColors();
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a new color")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "ApiBusinessErrorCodeDto.COLOR_INVALID_HEXA_CODE")
	})
	ColorDto createColor(@RequestBody String hexaCode);
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete an existing color")
	void deleteColor(@PathVariable("id") String id);
	
}