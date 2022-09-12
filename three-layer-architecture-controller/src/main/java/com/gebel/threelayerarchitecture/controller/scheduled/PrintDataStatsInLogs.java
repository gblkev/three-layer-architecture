package com.gebel.threelayerarchitecture.controller.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.service.interfaces.CarService;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class PrintDataStatsInLogs {

	private final ColorService colorService;
	private final DriverService driverService;
	private final CarService carService;
	
	@Scheduled(cron = "0 * * * * ?") // Every minute
	public void printDataStatsInLogs() {
		long colorsCount = colorService.countColors();
		long driversCount = driverService.countDrivers();
		long carsCount = carService.countCars();
		LOGGER.info("#######################");
		LOGGER.info("Stats:");
		LOGGER.info("    Colors in db: {}", colorsCount);
		LOGGER.info("    Drivers in db: {}", driversCount);
		LOGGER.info("    Cars in db: {}", carsCount);
		LOGGER.info("#######################");
	}
	
}