package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.business.service.converter.DomainDriverConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.dao.db.interfaces.DriverRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

	private final DriverRepository driverRepository;
	private final DomainDriverConverter driverConverter;
	
	@Override
	public List<Driver> getAllDrivers() {
		return driverConverter.toDomain(driverRepository.findAll());
	}
	
}