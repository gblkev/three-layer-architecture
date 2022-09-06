package com.gebel.threelayerarchitecture.controller.api.v1.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "A business error occured - no new attempt should be made without fixing the data")
public class ApiBusinessError extends ApiError {
	
	@Getter
	@Schema(required = true)
	private ApiBusinessErrorCode errorCode;
	
	public ApiBusinessError(String message, ApiBusinessErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
}