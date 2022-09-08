package com.gebel.threelayerarchitecture.controller.api.v2.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.controller.api.v2.converter.V2ApiBusinessErrorConverter;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorCodeDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiGenericErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiHttpRequestMethodNotSupportedErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiNotFoundErrorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiTechnicalErrorDto;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@ControllerAdvice
public class V2ApiExceptionHandler {

	private V2ApiBusinessErrorConverter businessErrorConverter;
	
	@ExceptionHandler(NoHandlerFoundException.class)
	protected ResponseEntity<ApiTechnicalErrorDto> handleNotFoundException(HttpRequestMethodNotSupportedException e) {
		ApiTechnicalErrorDto apiError = new ApiNotFoundErrorDto();
		LOGGER.info(e.getMessage());
		return new ResponseEntity<ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ApiTechnicalErrorDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		ApiTechnicalErrorDto apiError = new ApiHttpRequestMethodNotSupportedErrorDto();
		LOGGER.info(e.getMessage());
		return new ResponseEntity<ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	@ExceptionHandler(Throwable.class)
	@ApiResponse(
		responseCode = "500",
		description = "A technical error occured - a new attempt can be made",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = ApiTechnicalErrorDto.class)))
	protected ResponseEntity<ApiTechnicalErrorDto> handleGenericException(Throwable t) {
		ApiTechnicalErrorDto apiError = new ApiGenericErrorDto();
		LOGGER.error(apiError.getMessage(), t);
		return new ResponseEntity<ApiTechnicalErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
	// For business errors, @ApiResponse has to be specified on each web method.
	@ExceptionHandler(ApiBusinessException.class)
	protected ResponseEntity<ApiBusinessErrorDto> handleBusinessException(ApiBusinessException apiBusinessException) {
		BusinessException businessException = apiBusinessException.getBusinessException();
		ApiBusinessErrorCodeDto apiBusinessErrorCodeDto = businessErrorConverter.toDto(businessException.getBusinessError());
		ApiBusinessErrorDto apiError = new ApiBusinessErrorDto(apiBusinessErrorCodeDto.getDescription(), apiBusinessErrorCodeDto);
		LOGGER.info("Business error " + apiBusinessErrorCodeDto + " - " + businessException.getMessage());
		return new ResponseEntity<ApiBusinessErrorDto>(apiError, new HttpHeaders(), apiError.getHttpCode());
	}
	
}