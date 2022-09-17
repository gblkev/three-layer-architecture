package com.gebel.threelayerarchitecture.controller.api.v2.converter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Car;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.CarDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class V2ApiCarConverter {
	
	private final V2ApiColorConverter colorConverter;
	private final V2ApiDriverConverter driverConverter;
	
	public CarDto toDto(Car domainCar) {
		return CarDto.builder()
			.id(domainCar.getId())
			.color(colorConverter.toDto(domainCar.getColor()))
			.driver(driverConverter.toDto(domainCar.getDriver()))
			.build();
	}

	public List<CarDto> toDto(List<Car> domainCars) {
		return CollectionUtils.emptyIfNull(domainCars)
			.stream()
			.map(this::toDto)
			.toList();
	}

}