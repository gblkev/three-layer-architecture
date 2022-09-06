package com.gebel.threelayerarchitecture.controller.api.v1.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A technical error occured - a new attempt can be made")
public class ApiTechnicalError extends ApiError {

	public ApiTechnicalError(String message) {
		super(message);
	}
	
}