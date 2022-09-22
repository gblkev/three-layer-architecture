package com.gebel.threelayerarchitecture.controller.api.v2.error;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A technical error occured - a new attempt can be made")
public class ApiTechnicalErrorDto extends ApiErrorDto {
	
	private static final int MIN_HTTP_CODE = 400;
	private static final int MAX_HTTP_CODE = 599;
	
	public ApiTechnicalErrorDto() {
		super();
	}
	
	public ApiTechnicalErrorDto(String message, HttpStatus httpCode) {
		super(message, httpCode);
		checkHttpCode(httpCode);
	}
	
	private void checkHttpCode(HttpStatus httpCode) {
		if (httpCode == null || httpCode.value() < MIN_HTTP_CODE || httpCode.value() > MAX_HTTP_CODE) {
			throw new IllegalArgumentException("HTTP code '" + httpCode + "' should be contained between " + MIN_HTTP_CODE + " and " + MAX_HTTP_CODE);
		}
	}
	
}