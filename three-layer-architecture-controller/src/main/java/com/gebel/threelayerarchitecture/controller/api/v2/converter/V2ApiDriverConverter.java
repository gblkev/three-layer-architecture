package com.gebel.threelayerarchitecture.controller.api.v2.converter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.DriverDto;

@Component
public class V2ApiDriverConverter {
	
	public DriverDto toDto(Driver domainDriver) {
		return DriverDto.builder()
			.id(domainDriver.getId())
			.firstName(domainDriver.getFirstName())
			.lastName(domainDriver.getLastName())
			.build();
	}

	public List<DriverDto> toDto(List<Driver> domainDrivers) {
		return CollectionUtils.emptyIfNull(domainDrivers)
			.stream()
			.map(this::toDto)
			.toList();
	}

}