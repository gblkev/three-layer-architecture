package com.gebel.threelayerarchitecture.controller.api.converter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.controller.api.dto.ColorDto;

@Component
public class ApiColorConverter {
	
	public ColorDto toDto(Color domainColor) {
		return ColorDto.builder()
			.id(domainColor.getId())
			.hexaCode(domainColor.getHexaCode())
			.build();
	}

	public List<ColorDto> toDto(List<Color> domainColors) {
		return Optional.ofNullable(domainColors)
			.map(this::toDto)
			.orElse(Collections.emptyList());
	}

}