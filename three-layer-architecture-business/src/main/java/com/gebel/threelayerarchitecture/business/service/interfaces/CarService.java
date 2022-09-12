package com.gebel.threelayerarchitecture.business.service.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Car;

public interface CarService {
	
	List<Car> getAllCars();
	
	long countCars();
	
	Car createCar(String colorId, String driverId) throws BusinessException;
	
	void deleteCar(String carId);

}