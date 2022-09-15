package com.gebel.threelayerarchitecture.business.service.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.dao.mysql.entity.ColorEntity;

@Component
public class DomainColorConverter {
	
	public Color toDomain(ColorEntity entityColor) {
		if (entityColor == null) {
			return null;
		}
		return Color.builder()
			.id(entityColor.getId())
			.hexaCode(entityColor.getHexaCode())
			.build();
	}

	public List<Color> toDomain(List<ColorEntity> entitiesColors) {
		return CollectionUtils.emptyIfNull(entitiesColors)
			.stream()
			.map(this::toDomain)
			.collect(Collectors.toList());
	}
	
	public ColorEntity toEntity(Color domainColor) {
		if (domainColor == null) {
			return null;
		}
		return ColorEntity.builder()
			.id(domainColor.getId())
			.hexaCode(domainColor.getHexaCode())
			.build();
	}

}