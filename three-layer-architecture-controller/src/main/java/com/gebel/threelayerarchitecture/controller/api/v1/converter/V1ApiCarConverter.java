package com.gebel.threelayerarchitecture.controller.api.v1.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Car;
import com.gebel.threelayerarchitecture.controller.api.v1.dto.CarDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class V1ApiCarConverter {
	
	private final V1ApiColorConverter colorConverter;
	private final V1ApiDriverConverter driverConverter;
	
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
			.collect(Collectors.toList());
	}

}