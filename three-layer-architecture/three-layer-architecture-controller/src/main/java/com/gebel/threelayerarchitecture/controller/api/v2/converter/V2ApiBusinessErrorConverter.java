package com.gebel.threelayerarchitecture.controller.api.v2.converter;

import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.BusinessError;
import com.gebel.threelayerarchitecture.controller.api.v2.error.dto.ApiBusinessErrorCodeDto;

@Component
public class V2ApiBusinessErrorConverter {

	public ApiBusinessErrorCodeDto toDto(BusinessError businessError) {
		switch (businessError) {
			case COLOR_INVALID_HEXA_CODE: return ApiBusinessErrorCodeDto.COLOR_INVALID_HEXA_CODE;
			default: throw new IllegalArgumentException(businessError + " error code is not handled");
		}
	}
	
}