package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gebel.threelayerarchitecture.business.domain.BusinessErrorCode;
import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.business.service.converter.DomainDriverConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.dao.db.entity.DriverEntity;
import com.gebel.threelayerarchitecture.dao.db.interfaces.DriverRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

	private final DriverRepository driverRepository;
	private final DomainDriverConverter driverConverter;
	
	@Override
	@Transactional(readOnly = true)
	public List<Driver> getAllDrivers() {
		return driverConverter.toDomain(driverRepository.findAll());
	}

	@Override
	@Transactional
	public Driver createDriver(String firstName, String lastName) {
		Driver driver = Driver.builder()
			.firstName(firstName)
			.lastName(lastName)
			.build();
		checkDriverCreationPrerequisites(driver);
		return createDriverInDb(driver);
	}
	
	private void checkDriverCreationPrerequisites(Driver driver) throws BusinessException {
		driver.validate();
	}
	
	private Driver createDriverInDb(Driver driver) {
		DriverEntity driverEntity = driverConverter.toEntity(driver);
		driverEntity = driverRepository.save(driverEntity);
		return driverConverter.toDomain(driverEntity);
	}

	@Override
	@Transactional
	public void deleteDriver(String driverId) {
		if (driverRepository.existsById(driverId)) {
			driverRepository.deleteById(driverId);
		}
	}
	
}