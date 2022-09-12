package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Driver;
import com.gebel.threelayerarchitecture.business.service.converter.DomainDriverConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;
import com.gebel.threelayerarchitecture.dao.db.entity.DriverEntity;
import com.gebel.threelayerarchitecture.dao.db.interfaces.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

	private final DriverRepository driverRepository;
	private final DomainDriverConverter driverConverter;
	
	@Override
	@Transactional(readOnly = true)
	public Driver getDriverById(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		Optional<DriverEntity> optionalEntity = driverRepository.findById(id);
		if (optionalEntity.isEmpty()) {
			return null;
		}
		return driverConverter.toDomain(optionalEntity.get());
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Driver> getAllDrivers() {
		return driverConverter.toDomain(driverRepository.findAll());
	}

	@Override
	@Transactional
	public Driver createDriver(String firstName, String lastName) throws BusinessException {
		Driver driver = Driver.builder()
			.firstName(firstName)
			.lastName(lastName)
			.build();
		checkDriverCreationPrerequisites(driver);
		return createDriverInDb(driver);
	}
	
	private void checkDriverCreationPrerequisites(Driver driver) throws BusinessException {
		driver.cleanAndValidate();
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