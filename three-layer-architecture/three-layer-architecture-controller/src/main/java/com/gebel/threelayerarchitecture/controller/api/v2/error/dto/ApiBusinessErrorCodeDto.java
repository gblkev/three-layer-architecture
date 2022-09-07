package com.gebel.threelayerarchitecture.controller.api.v2.error.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiBusinessErrorCodeDto {
	
	COLOR_INVALID_HEXA_CODE("The hexadecimal code of the color is not valid");
	
	private String description;
	
}