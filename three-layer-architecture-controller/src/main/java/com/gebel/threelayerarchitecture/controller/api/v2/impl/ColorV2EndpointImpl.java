package com.gebel.threelayerarchitecture.controller.api.v2.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.controller.api.v2.converter.V2ApiColorConverter;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.ColorV2Endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class ColorV2EndpointImpl implements ColorV2Endpoint {

	private final ColorService colorService;
	private final V2ApiColorConverter colorConverter;
	
	@Override
	public List<ColorDto> getAllColors() {
		LOGGER.info("Listing all colors");
		return colorConverter.toDto(colorService.getAllColors());
	}

	@Override
	public ColorDto createColor(String hexaCode) throws BusinessException {
		LOGGER.info("Creating color with hexaCode={}", hexaCode);
		return colorConverter.toDto(colorService.createColor(hexaCode));
	}

	@Override
	public void deleteColor(String colorId) {
		LOGGER.info("Deleting color with id={}", colorId);
		colorService.deleteColor(colorId);
	}

}