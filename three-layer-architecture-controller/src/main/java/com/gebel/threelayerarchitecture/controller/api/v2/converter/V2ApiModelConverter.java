package com.gebel.threelayerarchitecture.controller.api.v2.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Model;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.ModelDto;

@Component
public class V2ApiModelConverter {
	
	public ModelDto toDto(Model domainModel) {
		return ModelDto.builder()
			.id(domainModel.getId())
			.name(domainModel.getName())
			.build();
	}

	public List<ModelDto> toDto(List<Model> domainModels) {
		return CollectionUtils.emptyIfNull(domainModels)
			.stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

}