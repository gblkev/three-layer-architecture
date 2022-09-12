package com.gebel.threelayerarchitecture.controller.scheduled;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.gebel.threelayerarchitecture.business.domain.DataReport;
import com.gebel.threelayerarchitecture.business.service.interfaces.DataReportService;
import com.gebel.threelayerarchitecture.controller._test.TestContainersManager;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PrintDataReportInLogsIT {
	
	private static final TestContainersManager TEST_CONTAINERS_MANAGER = new TestContainersManager(); // Shared between all methods.
	
	@SpyBean
	private PrintDataReportInLogs printDataReportInLogs;
	
	@SpyBean
	private DataReportService dataReportService;
	
	@DynamicPropertySource
	private static void setupContainersDynamicConfigurationProperties(DynamicPropertyRegistry registry) throws IOException {
		TEST_CONTAINERS_MANAGER.startContainers();
		TEST_CONTAINERS_MANAGER.setDynamicContainersConfiguration(registry);
	}
	
	@AfterAll
	private static void clearAll() {
		TEST_CONTAINERS_MANAGER.stopContainers();
	}
	
	@Test
	void givenDataReportGenerated_whenScheduledPrintDataReportInLogs_thenDataReportPrintedOutInLogs() {
		// Given
		DataReport dataReport = DataReport.builder()
			.colorsCount(1)
			.driversCount(2)
			.carsCount(3)
			.build();
		Mockito.when(dataReportService.generateDataReport())
			.thenReturn(dataReport);
		
		// When
		printDataReportInLogs.scheduledPrintDataReportInLogs();
		
		// Then
		// TODO
//		implement an in-memory appender
	}
		
	// TODO name - test scheduling
	@Test
	void given_when_then2() {
		// Given
		
		// When
		
		// Then
//		await()
//			.atMost(Duration.ofSeconds(5))
//			.untilAsserted(() -> verify(printDataStatsInLogs, times(1)).printDataStatsInLogs());
	}

}