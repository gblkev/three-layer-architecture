package com.gebel.threelayerarchitecture.controller.api.v2.impl;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.controller.api.v2.dto.FutureDto;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.FutureV2Endpoint;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class FutureV2EndpointImpl implements FutureV2Endpoint {

	@Override
	public FutureDto readFuture() {
		return FutureDto.builder()
				.message("This is a future feature that is not released yet")
				.build();
	}

}