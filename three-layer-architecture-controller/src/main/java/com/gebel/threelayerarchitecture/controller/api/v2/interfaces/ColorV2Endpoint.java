package com.gebel.threelayerarchitecture.controller.api.v2.interfaces;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V2ApiBaseUri.API_V2_BASE_URI + "/colors")
@Tag(name = "Colors")
public interface ColorV2Endpoint {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "List all the available colors")
	List<ColorDto> getAllAvailableColors();
	
	@PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a new color")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "ApiBusinessErrorCodeDto.COLOR_INVALID_HEXA_CODE"),
		@ApiResponse(responseCode = "ApiBusinessErrorCodeDto.COLOR_SAME_HEXA_CODE_ALREADY_EXISTS")
	})
	ColorDto createColor(@RequestBody @Schema(description = "Hexadecimal code", required = true, example = "#000000") String hexaCode);
	
	@DeleteMapping("/{colorId}")
	@Operation(summary = "Delete an existing color")
	void deleteColor(@PathVariable("colorId") String colorId);
	
}