package com.gebel.threelayerarchitecture.controller.api.v1.error;

import org.springframework.http.HttpStatus;

public class ApiNotFoundErrorDto extends ApiTechnicalErrorDto {

	private static final String MESSAGE = "Page not found";
	private static final HttpStatus HTTP_CODE = HttpStatus.NOT_FOUND;
	
	public ApiNotFoundErrorDto() {
		super(MESSAGE, HTTP_CODE);
	}
	
}