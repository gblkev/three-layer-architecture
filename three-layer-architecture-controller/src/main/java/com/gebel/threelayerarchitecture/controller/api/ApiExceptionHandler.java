package com.gebel.threelayerarchitecture.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiBusinessErrorConverter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

	private final V1ApiBusinessErrorConverter v1BusinessErrorConverter;
	
	@ExceptionHandler(NoHandlerFoundException.class)
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto> handleNotFoundException(NoHandlerFoundException e) {
		com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v1.error.ApiNotFoundErrorDto();
		LOGGER.info(e.getMessage());
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v1.error.ApiHttpRequestMethodNotSupportedErrorDto();
		LOGGER.info(e.getMessage());
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	@ExceptionHandler(Throwable.class)
	@ApiResponse(
		responseCode = "500",
		description = "A technical error occured - a new attempt can be made",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto.class)))
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto> handleGenericException(Throwable t) {
		com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v1.error.ApiGenericErrorDto();
		LOGGER.error(apiError.getMessage(), t);
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	// For business errors, @ApiResponse has to be specified on each web method.
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessErrorDto> handleBusinessException(BusinessException businessException) {
		com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessErrorCodeDto apiBusinessErrorCodeDto =
			v1BusinessErrorConverter.toDto(businessException.getBusinessError());
		com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessErrorDto(apiBusinessErrorCodeDto.getDescription(), apiBusinessErrorCodeDto);
		LOGGER.info("Business error " + apiBusinessErrorCodeDto + " - " + businessException.getMessage());
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
}