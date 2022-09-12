package com.gebel.threelayerarchitecture.controller.api.v1.converter;

import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.BusinessErrorCode;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorCodeDto;

@Component
public class V1ApiBusinessErrorConverter {

	public ApiBusinessErrorCodeDto toDto(BusinessErrorCode businessError) {
		switch (businessError) {
		
			// Color
			case COLOR_INVALID_HEXA_CODE: return ApiBusinessErrorCodeDto.COLOR_INVALID_HEXA_CODE;
			case COLOR_SAME_HEXA_CODE_ALREADY_EXISTS: return ApiBusinessErrorCodeDto.COLOR_SAME_HEXA_CODE_ALREADY_EXISTS;
			
			// Driver
			case DRIVER_INVALID_FIRST_NAME: return ApiBusinessErrorCodeDto.DRIVER_INVALID_FIRST_NAME;
			case DRIVER_INVALID_LAST_NAME: return ApiBusinessErrorCodeDto.DRIVER_INVALID_LAST_NAME;
			
			// Car
			case CAR_INVALID_COLOR: return ApiBusinessErrorCodeDto.CAR_INVALID_COLOR;
			case CAR_INVALID_DRIVER: return ApiBusinessErrorCodeDto.CAR_INVALID_DRIVER;
			
			default: throw new IllegalArgumentException(businessError + " error code is not handled");
		}
	}
	
}