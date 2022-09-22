package com.gebel.threelayerarchitecture.business.service.converter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Brand;
import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DomainBrandConverter {
	
	private final DomainModelConverter domainModelConverter;
	
	public Brand toDomain(CarBrandModel carBrandModel) {
		if (carBrandModel == null) {
			return null;
		}
		return Brand.builder()
			.id(carBrandModel.getId())
			.name(carBrandModel.getName())
			.models(domainModelConverter.toDomain(carBrandModel.getModels()))
			.build();
	}

	public List<Brand> toDomain(List<CarBrandModel> carBrandsModels) {
		return CollectionUtils.emptyIfNull(carBrandsModels)
			.stream()
			.map(this::toDomain)
			.toList();
	}

}