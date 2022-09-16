package com.gebel.threelayerarchitecture.business.service.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Model;
import com.gebel.threelayerarchitecture.dao.redis.model.CarModelModel;

@Component
public class DomainModelConverter {

	public Model toDomain(CarModelModel modelModel) {
		if (modelModel == null) {
			return null;
		}
		return Model.builder()
			.id(modelModel.getId())
			.name(modelModel.getName())
			.build();
	}

	public List<Model> toDomain(List<CarModelModel> modelsModels) {
		return CollectionUtils.emptyIfNull(modelsModels)
			.stream()
			.map(this::toDomain)
			.collect(Collectors.toList());
	}
	
}