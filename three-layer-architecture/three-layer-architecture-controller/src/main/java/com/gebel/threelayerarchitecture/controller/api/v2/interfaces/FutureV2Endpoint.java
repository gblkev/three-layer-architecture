package com.gebel.threelayerarchitecture.controller.api.v2.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.v2.dto.FutureDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V2ApiBaseUri.API_V2_BASE_URI + "/future")
@Tag(name = "Future", description = "Soon to be released")
public interface FutureV2Endpoint {

	@GetMapping
	@Operation(summary = "Give a int about the future")
	FutureDto readFuture();
	
}