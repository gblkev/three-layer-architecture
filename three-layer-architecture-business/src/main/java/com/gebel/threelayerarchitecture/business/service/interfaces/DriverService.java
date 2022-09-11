package com.gebel.threelayerarchitecture.business.service.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.business.domain.Driver;

public interface DriverService {
	
	List<Driver> getAllDrivers();
	
	Driver createDriver(String firstName, String lastName);
	
	void deleteDriver(String driverId);

}