package com.gebel.threelayerarchitecture.controller.api.v2.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessException;
import com.gebel.threelayerarchitecture.controller.api.v2.converter.V2ApiColorConverter;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.ColorV2Endpoint;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ColorV2EndpointImpl implements ColorV2Endpoint {

	private final ColorService colorService;
	private final V2ApiColorConverter colorConverter;
	
	@Override
	public List<ColorDto> getAllAvailableColors() {
		return colorConverter.toDto(colorService.getAllAvailableColors());
	}

	@Override
	public ColorDto createColor(String hexaCode) {
		try {
			return colorConverter.toDto(colorService.createColor(hexaCode));
		}
		catch (BusinessException businessException) {
			throw new ApiBusinessException(businessException);
		}
	}

	@Override
	public void deleteColor(String id) {
		colorService.deleteColor(id);
	}

}