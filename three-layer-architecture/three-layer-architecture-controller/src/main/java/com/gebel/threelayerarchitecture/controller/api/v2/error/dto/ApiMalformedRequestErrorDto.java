package com.gebel.threelayerarchitecture.controller.api.v2.error.dto;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A malformede request error occured - no new attempt should be made without fixing the data")
public class ApiMalformedRequestErrorDto extends ApiErrorDto {
	
	protected static final HttpStatus HTTP_CODE = HttpStatus.BAD_REQUEST;
	
	public ApiMalformedRequestErrorDto(String message) {
		super(message, HTTP_CODE);
	}

}