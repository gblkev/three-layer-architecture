package com.gebel.threelayerarchitecture.controller.api.v2.converter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ColorDto;

@Component
public class V2ApiColorConverter {
	
	public ColorDto toDto(Color domainColor) {
		return ColorDto.builder()
			.id(domainColor.getId())
			.hexaCode(domainColor.getHexaCode())
			.build();
	}

	public List<ColorDto> toDto(List<Color> domainColors) {
		return CollectionUtils.emptyIfNull(domainColors)
			.stream()
			.map(this::toDto)
			.toList();
	}

}