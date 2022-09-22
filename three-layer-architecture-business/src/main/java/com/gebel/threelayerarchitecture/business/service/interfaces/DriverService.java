package com.gebel.threelayerarchitecture.business.service.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Driver;

public interface DriverService {
	
	Driver getDriverById(String id);
	
	List<Driver> getAllDrivers();
	
	long countDrivers();
	
	Driver createDriver(String firstName, String lastName) throws BusinessException;
	
	void deleteDriver(String driverId);

}