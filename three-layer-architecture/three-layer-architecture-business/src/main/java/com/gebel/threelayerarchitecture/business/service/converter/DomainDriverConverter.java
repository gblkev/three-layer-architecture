package com.gebel.threelayerarchitecture.business.service.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.dao.db.entity.DriverEntity;

@Component
public class DomainDriverConverter {
	
	public Driver toDomain(DriverEntity entityDriver) {
		return Driver.builder()
			.id(entityDriver.getId())
			.firstName(entityDriver.getFirstName())
			.lastName(entityDriver.getLastName())
			.build();
	}

	public List<Driver> toDomain(List<DriverEntity> entitiesDrivers) {
		return CollectionUtils.emptyIfNull(entitiesDrivers)
			.stream()
			.map(this::toDomain)
			.collect(Collectors.toList());
	}

}