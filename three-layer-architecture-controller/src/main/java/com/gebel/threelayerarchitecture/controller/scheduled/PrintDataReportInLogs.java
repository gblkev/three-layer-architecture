package com.gebel.threelayerarchitecture.controller.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.DataReport;
import com.gebel.threelayerarchitecture.business.service.interfaces.DataReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintDataReportInLogs {

	private final DataReportService dataReportService;
	
	@Scheduled(cron = "0 18 * * *") // Every day at 18h.
	public void scheduledPrintDataReportInLogs() {
		try {
			printDataDataInLogs();
		}
		catch (Exception e) {
			LOGGER.error("Error during the execution of the scheduled task " + PrintDataReportInLogs.class.getSimpleName(), e);
		}
	}
	
	private void printDataDataInLogs() {
		DataReport dataReport = dataReportService.generateDataReport();
		LOGGER.info("#######################");
		LOGGER.info("Data report:");
		LOGGER.info("    Colors in db: {}", dataReport.getColorsCount());
		LOGGER.info("    Drivers in db: {}", dataReport.getDriversCount());
		LOGGER.info("    Cars in db: {}", dataReport.getCarsCount());
		LOGGER.info("#######################");
	}
	
}