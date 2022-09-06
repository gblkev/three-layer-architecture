package com.gebel.threelayerarchitecture.controller.api.v1.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiBusinessErrorConverter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class V1ApiExceptionHandler {

	private static final String GENERIC_TECHNICAL_ERROR_MESSAGE = "An unexpected error occured";
	private static final String GENERIC_BUSINESS_ERROR_MESSAGE = "A business error occured";
	
	private V1ApiBusinessErrorConverter businessErrorConverter;
	
	@ExceptionHandler(Throwable.class)
	@ApiResponse(responseCode = "500", description = "A technical error occured - a new attempt can be made")
	protected ResponseEntity<ApiTechnicalError> handleGenericException(Throwable t) {
		ApiTechnicalError apiError = new ApiTechnicalError(GENERIC_TECHNICAL_ERROR_MESSAGE);
		LOGGER.error(GENERIC_TECHNICAL_ERROR_MESSAGE, t);
		return new ResponseEntity<ApiTechnicalError>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
	
	@ExceptionHandler(ApiBusinessException.class)
	protected ResponseEntity<ApiBusinessError> handleBusinessException(ApiBusinessException apiBusinessException) {
		BusinessException businessException = apiBusinessException.getBusinessException();
		ApiBusinessError apiError = new ApiBusinessError(GENERIC_BUSINESS_ERROR_MESSAGE, businessErrorConverter.toDto(businessException.getBusinessError()));
		LOGGER.info(GENERIC_BUSINESS_ERROR_MESSAGE + " - " + apiError.getErrorCode());
		return new ResponseEntity<ApiBusinessError>(apiError, new HttpHeaders(), HttpStatus.CONFLICT.value());
	}
	
}