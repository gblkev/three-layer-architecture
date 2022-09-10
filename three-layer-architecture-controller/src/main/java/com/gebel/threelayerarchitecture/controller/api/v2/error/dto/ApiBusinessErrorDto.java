package com.gebel.threelayerarchitecture.controller.api.v2.error.dto;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "A business error occured - no new attempt should be made without fixing the data")
public class ApiBusinessErrorDto extends ApiErrorDto {
	
	public static final HttpStatus CONFLICT_HTTP_CODE = HttpStatus.CONFLICT;
	
	@Schema(required = true)
	private ApiBusinessErrorCodeDto errorCode;
	
	public ApiBusinessErrorDto() {
		super(null, CONFLICT_HTTP_CODE);
	}
	
	public ApiBusinessErrorDto(String message, ApiBusinessErrorCodeDto errorCode) {
		super(message, CONFLICT_HTTP_CODE);
		this.errorCode = errorCode;
	}
	
}