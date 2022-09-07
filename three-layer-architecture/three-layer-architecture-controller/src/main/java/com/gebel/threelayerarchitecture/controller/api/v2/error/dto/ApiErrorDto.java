package com.gebel.threelayerarchitecture.controller.api.v2.error.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class ApiErrorDto {

	@Schema(required = true)
	private String message;
	
	@JsonIgnore
	private HttpStatus httpCode;
	
}