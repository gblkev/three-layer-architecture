package com.gebel.threelayerarchitecture.controller.api.v1.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class ApiError {

	@Schema(required = true)
	private String message;
	
}