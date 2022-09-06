package com.gebel.threelayerarchitecture.controller.api.v1.converter;

import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.BusinessError;
import com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessErrorCode;

@Component
public class V1ApiBusinessErrorConverter {

	public ApiBusinessErrorCode toDto(BusinessError businessError) {
		switch (businessError) {
			case COLOR_INVALID_HEXA_CODE: return ApiBusinessErrorCode.COLOR_INVALID_HEXA_CODE;
			default: throw new IllegalArgumentException(businessError + " error code is not handled");
		}
	}
	
}