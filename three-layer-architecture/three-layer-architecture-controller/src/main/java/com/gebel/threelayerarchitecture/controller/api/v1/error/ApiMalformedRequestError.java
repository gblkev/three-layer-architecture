package com.gebel.threelayerarchitecture.controller.api.v1.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A malformede request error occured - no new attempt should be made without fixing the data")
public class ApiMalformedRequestError extends ApiError {
	
	public ApiMalformedRequestError(String message) {
		super(message);
	}

}