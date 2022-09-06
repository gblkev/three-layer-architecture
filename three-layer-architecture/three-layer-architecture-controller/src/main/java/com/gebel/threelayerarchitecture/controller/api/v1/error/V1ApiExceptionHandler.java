package com.gebel.threelayerarchitecture.controller.api.v1.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiBusinessErrorConverter;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto;

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
	@ApiResponseTechnicalError(responseCode = ApiTechnicalErrorDto.GENERIC_ERROR_HTTP_CODE, description = "A technical error occured - a new attempt can be made")
	protected ResponseEntity<ApiTechnicalErrorDto> handleGenericException(Throwable t) {
		ApiTechnicalErrorDto apiError = new ApiTechnicalErrorDto(GENERIC_TECHNICAL_ERROR_MESSAGE, ApiTechnicalErrorDto.GENERIC_ERROR_HTTP_CODE);
		LOGGER.error(GENERIC_TECHNICAL_ERROR_MESSAGE, t);
		return new ResponseEntity<ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	@ExceptionHandler(ApiBusinessException.class)
	protected ResponseEntity<ApiBusinessErrorDto> handleBusinessException(ApiBusinessException apiBusinessException) {
		BusinessException businessException = apiBusinessException.getBusinessException();
		ApiBusinessErrorDto apiError = new ApiBusinessErrorDto(GENERIC_BUSINESS_ERROR_MESSAGE, businessErrorConverter.toDto(businessException.getBusinessError()));
		LOGGER.info(GENERIC_BUSINESS_ERROR_MESSAGE + " - " + apiError.getErrorCode());
		return new ResponseEntity<ApiBusinessErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
}