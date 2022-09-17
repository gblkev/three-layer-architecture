package com.gebel.threelayerarchitecture.business.service.converter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Car;
import com.gebel.threelayerarchitecture.dao.mysql.entity.CarEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DomainCarConverter {
	
	private final DomainColorConverter domainColorConverter;
	private final DomainDriverConverter domainDriverConverter;
	
	public Car toDomain(CarEntity entityCar) {
		if (entityCar == null) {
			return null;
		}
		return Car.builder()
			.id(entityCar.getId())
			.color(domainColorConverter.toDomain(entityCar.getColor()))
			.driver(domainDriverConverter.toDomain(entityCar.getDriver()))
			.build();
	}

	public List<Car> toDomain(List<CarEntity> entitiesCars) {
		return CollectionUtils.emptyIfNull(entitiesCars)
			.stream()
			.map(this::toDomain)
			.toList();
	}
	
	public CarEntity toEntity(Car domainCar) {
		if (domainCar == null) {
			return null;
		}
		return CarEntity.builder()
			.id(domainCar.getId())
			.color(domainColorConverter.toEntity(domainCar.getColor()))
			.driver(domainDriverConverter.toEntity(domainCar.getDriver()))
			.build();
	}

}