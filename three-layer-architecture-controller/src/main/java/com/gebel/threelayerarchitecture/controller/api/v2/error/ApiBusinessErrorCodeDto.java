package com.gebel.threelayerarchitecture.controller.api.v2.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiBusinessErrorCodeDto {
	
	// Color
	COLOR_INVALID_HEXA_CODE("The hexadecimal code of the color is not valid"),
	COLOR_SAME_HEXA_CODE_ALREADY_EXISTS("A color with the same hexadecimal code already exists"),
	
	// Driver
	DRIVER_INVALID_FIRST_NAME("The first name of the driver is not valid"),
	DRIVER_INVALID_LAST_NAME("The last name of the driver is not valid"),
	
	// Car
	CAR_INVALID_COLOR("The color associated with the car is not valid"),
	CAR_INVALID_DRIVER("The driver associated with the car is not valid");
	
	private String description;
	
}