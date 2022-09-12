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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class ColorV1EndpointImpl implements ColorV1Endpoint {

	private final ColorService colorService;
	private final V1ApiColorConverter colorConverter;
	
	@Override
	public List<ColorDto> getAllColors() {
		LOGGER.info("Listing all available colors");
		return colorConverter.toDto(colorService.getAllColors());
	}

	@Override
	public ColorDto createColor(String hexaCode) {
		try {
			LOGGER.info("Creating color with hexaCode={}", hexaCode);
			return colorConverter.toDto(colorService.createColor(hexaCode));
		}
		catch (BusinessException businessException) {
			throw new ApiBusinessException(businessException);
		}
	}

	@Override
	public void deleteColor(String colorId) {
		LOGGER.info("Deleting color with id={}", colorId);
		colorService.deleteColor(colorId);
	}

}