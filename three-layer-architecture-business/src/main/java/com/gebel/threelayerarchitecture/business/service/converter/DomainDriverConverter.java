package com.gebel.threelayerarchitecture.business.service.converter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.dao.mysql.entity.DriverEntity;

@Component
public class DomainDriverConverter {
	
	public Driver toDomain(DriverEntity entityDriver) {
		if (entityDriver == null) {
			return null;
		}
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
			.toList();
	}
	
	public DriverEntity toEntity(Driver domainDriver) {
		if (domainDriver == null) {
			return null;
		}
		return DriverEntity.builder()
			.id(domainDriver.getId())
			.firstName(domainDriver.getFirstName())
			.lastName(domainDriver.getLastName())
			.build();
	}

}