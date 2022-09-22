package com.gebel.threelayerarchitecture.controller.api.v1.error;

import org.springframework.http.HttpStatus;

public class ApiGenericErrorDto extends ApiTechnicalErrorDto {

	private static final String MESSAGE = "An unexpected error occured";
	private static final HttpStatus HTTP_CODE = HttpStatus.INTERNAL_SERVER_ERROR;
	
	public ApiGenericErrorDto() {
		super(MESSAGE, HTTP_CODE);
	}
	
}