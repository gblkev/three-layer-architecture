package com.gebel.threelayerarchitecture.controller.api.v2.interfaces;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V2ApiBaseUri.API_V2_BASE_URI + "/colors")
@Tag(name = "Colors")
public interface ColorV2Endpoint {

	@GetMapping
	@Operation(summary = "List all the available colors")
	List<ColorDto> getAllAvailableColors();
	
	@PostMapping
	@Operation(summary = "Create a new color")
	ColorDto createColor(@RequestBody String hexaCode);
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete an existing color")
	void deleteColor(@PathVariable("id") String id);
	
}