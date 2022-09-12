package com.gebel.threelayerarchitecture.controller.api.v1.converter;

import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.BusinessErrorCode;
import com.gebel.threelayerarchitecture.controller.api.v1.error.dto.ApiBusinessErrorCodeDto;

@Component
public class V1ApiBusinessErrorConverter {

	public ApiBusinessErrorCodeDto toDto(BusinessErrorCode businessError) {
		switch (businessError) {
			case COLOR_INVALID_HEXA_CODE: return ApiBusinessErrorCodeDto.COLOR_INVALID_HEXA_CODE;
			case COLOR_SAME_HEXA_CODE_ALREADY_EXISTS: return ApiBusinessErrorCodeDto.COLOR_SAME_HEXA_CODE_ALREADY_EXISTS;
			case DRIVER_INVALID_FIRST_NAME: return ApiBusinessErrorCodeDto.DRIVER_INVALID_FIRST_NAME;
			case DRIVER_INVALID_LAST_NAME: return ApiBusinessErrorCodeDto.DRIVER_INVALID_LAST_NAME;
			default: throw new IllegalArgumentException(businessError + " error code is not handled");
		}
	}
	
}