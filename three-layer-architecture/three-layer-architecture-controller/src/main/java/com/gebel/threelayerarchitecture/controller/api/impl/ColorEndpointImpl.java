package com.gebel.threelayerarchitecture.controller.api.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.controller.api.converter.ApiColorConverter;
import com.gebel.threelayerarchitecture.controller.api.dto.ColorDto;
import com.gebel.threelayerarchitecture.controller.api.interfaces.ColorEndpoint;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ColorEndpointImpl implements ColorEndpoint {

	private final ColorService colorService;
	private final ApiColorConverter colorConverter;
	
	@Override
	public List<ColorDto> getAllAvailableColors() {
		return colorConverter.toDto(colorService.getAllAvailableColors());
	}

}