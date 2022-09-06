package com.gebel.threelayerarchitecture.controller.api.v1.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiBusinessErrorCode {
	
	COLOR_INVALID_HEXA_CODE("The hexadecimal code of the color is not valid");
	
	private String description;
	
}