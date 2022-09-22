package com.gebel.threelayerarchitecture.controller.api.v2.interfaces;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.v2.dto.BrandDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V2ApiBaseUri.API_V2_BASE_URI + "/brands")
@Tag(name = "Brands")
public interface BrandV2Endpoint {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "List all the brands and associated models")
	List<BrandDto> getAllBrands();
	
}