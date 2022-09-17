package com.gebel.threelayerarchitecture.controller.api.v1.error;

import org.springframework.http.HttpStatus;

public class ApiHttpRequestMethodNotSupportedErrorDto extends ApiTechnicalErrorDto {

	private static final String MESSAGE = "Http request method not supported";
	private static final HttpStatus HTTP_CODE = HttpStatus.METHOD_NOT_ALLOWED;
	
	public ApiHttpRequestMethodNotSupportedErrorDto() {
		super(MESSAGE, HTTP_CODE);
	}
	
}