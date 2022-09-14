package com.gebel.threelayerarchitecture.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiBusinessErrorConverter;
import com.gebel.threelayerarchitecture.controller.api.v2.converter.V2ApiBusinessErrorConverter;

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
	private final V2ApiBusinessErrorConverter v2BusinessErrorConverter;
	
	@ExceptionHandler(NoHandlerFoundException.class)
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto> handleNotFoundException(NoHandlerFoundException e) {
		com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiNotFoundErrorDto();
		LOGGER.info(e.getMessage());
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiHttpRequestMethodNotSupportedErrorDto();
		LOGGER.info(e.getMessage());
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	@ExceptionHandler(Throwable.class)
	@ApiResponse(
		responseCode = "500",
		description = "A technical error occured - a new attempt can be made",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto.class)))
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto> handleGenericException(Throwable t) {
		com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiGenericErrorDto();
		LOGGER.error(apiError.getMessage(), t);
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	// For business errors, @ApiResponse has to be specified on each web method.
	@ExceptionHandler(com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessException.class)
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorDto> handleV1BusinessException(
			com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessException apiBusinessException) {
		BusinessException businessException = apiBusinessException.getBusinessException();
		com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorCodeDto apiBusinessErrorCodeDto =
			v1BusinessErrorConverter.toDto(businessException.getBusinessError());
		com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorDto(apiBusinessErrorCodeDto.getDescription(), apiBusinessErrorCodeDto);
		LOGGER.info("Business error " + apiBusinessErrorCodeDto + " - " + businessException.getMessage());
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	// For business errors, @ApiResponse has to be specified on each web method.
	@ExceptionHandler(com.gebel.threelayerarchitecture.controller.api.v2.error.ApiBusinessException.class)
	protected ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorDto> handleV2BusinessException(
			com.gebel.threelayerarchitecture.controller.api.v2.error.ApiBusinessException apiBusinessException) {
		BusinessException businessException = apiBusinessException.getBusinessException();
		com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorCodeDto apiBusinessErrorCodeDto =
			v2BusinessErrorConverter.toDto(businessException.getBusinessError());
		com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorDto apiError =
			new com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorDto(apiBusinessErrorCodeDto.getDescription(), apiBusinessErrorCodeDto);
		LOGGER.info("Business error " + apiBusinessErrorCodeDto + " - " + businessException.getMessage());
		return new ResponseEntity<com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
}