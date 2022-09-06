package com.gebel.threelayerarchitecture.controller.api.v1.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.controller.api.v1.converter.V1ApiColorConverter;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.api.v1.error.ApiBusinessException;
import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.ColorV1Endpoint;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ColorV1EndpointImpl implements ColorV1Endpoint {

	private final ColorService colorService;
	private final V1ApiColorConverter colorConverter;
	
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