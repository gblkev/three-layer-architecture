package com.gebel.threelayerarchitecture.business.service.impl;

import org.springframework.stereotype.Service;

import com.gebel.threelayerarchitecture.business.domain.DataReport;
import com.gebel.threelayerarchitecture.business.service.interfaces.CarService;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.business.service.interfaces.DataReportService;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataReportServiceImpl implements DataReportService {
	
	private final ColorService colorService;
	private final DriverService driverService;
	private final CarService carService;

	@Override
	public DataReport generateDataReport() {
		long colorsCount = colorService.countColors();
		long driversCount = driverService.countDrivers();
		long carsCount = carService.countCars();
		return new DataReport(colorsCount, driversCount, carsCount);
	}

}