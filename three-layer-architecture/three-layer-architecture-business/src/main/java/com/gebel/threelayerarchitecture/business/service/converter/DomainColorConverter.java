package com.gebel.threelayerarchitecture.business.service.converter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.dao.db.entity.ColorEntity;

@Component
public class DomainColorConverter {
	
	public Color toDomain(ColorEntity entityColor) {
		return Color.builder()
			.id(entityColor.getId())
			.hexaCode(entityColor.getHexaCode())
			.build();
	}

	public List<Color> toDomain(List<ColorEntity> entitiesColors) {
		return Optional.ofNullable(entitiesColors)
			.map(this::toDomain)
			.orElse(Collections.emptyList());
	}

}