package com.gebel.threelayerarchitecture.controller.api.v2.error.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiBusinessErrorCodeDto {
	
	COLOR_INVALID_HEXA_CODE("The hexadecimal code of the color is not valid"),
	COLOR_SAME_HEXA_CODE_ALREADY_EXISTS("A color with the same hexadecimal code already exists");
	
	private String description;
	
}