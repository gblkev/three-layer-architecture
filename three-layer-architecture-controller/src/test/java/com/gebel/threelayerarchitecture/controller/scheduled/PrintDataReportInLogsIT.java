package com.gebel.threelayerarchitecture.controller.scheduled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import com.gebel.threelayerarchitecture.controller._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.controller._test.Log4jInMemoryAppender;

@SpringBootTest(
	webEnvironment = WebEnvironment.RANDOM_PORT,
	// To speed up the error occurrence when we simulate a connection issue with the database.
	properties = {
		"spring.datasource.hikari.connection-timeout=250",
		"spring.datasource.hikari.validation-timeout=250"
	}
)
class PrintDataReportInLogsIT extends AbstractIntegrationTest {
	
	@SpyBean
	private PrintDataReportInLogs printDataReportInLogs;
	
	@SpyBean
	private ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor;
	
	@Test
	void givenDataReportGenerated_whenScheduledPrintDataReportInLogs_thenDataReportPrintedInLogs() throws Exception {
		Log4jInMemoryAppender log4jInMemoryAppender = new Log4jInMemoryAppender(PrintDataReportInLogs.class);
		try {
			// Given
			getTestContainers().getMysqlTestContainer().executeSqlScript("scheduled/printDataReportInLogs/createSeveralCars.sql");
			getTestContainers().getRedisTestContainer().executeCommandsScript("scheduled/printDataReportInLogs/createSeveralBrands");
			log4jInMemoryAppender.setupAppender();
			
			// When
			printDataReportInLogs.scheduledPrintDataReportInLogs();
			
			// Then
			List<String> expectedLogs = List.of(
				"#######################",
				"Data report:",
				"    Total colors: 4",
				"    Total drivers: 2",
				"    Total cars: 6",
				"    Total brands: 3",
				"#######################");
			List<String> eventsMessagesAsString = log4jInMemoryAppender.getEventsMessagesAsString();
			assertEquals(expectedLogs, eventsMessagesAsString);
		}
		finally {
			log4jInMemoryAppender.removeAppender();
		}
	}
	
	@Test
	void givenDatabaseUnavailable_whenScheduledPrintDataReportInLogs_thenErrorInLogs() {
		Log4jInMemoryAppender log4jInMemoryAppender = new Log4jInMemoryAppender(PrintDataReportInLogs.class);
		try {
			// Given + sql
			log4jInMemoryAppender.setupAppender();
			getTestContainers().getMysqlTestContainer().pause();
			
			// When
			printDataReportInLogs.scheduledPrintDataReportInLogs();
			
			// Then
			String expectedLog = "Error during the execution of the scheduled task PrintDataReportInLogs";
			for (LogEvent logEvent : log4jInMemoryAppender.getEvents()) {
				if (logEvent.getMessage().getFormattedMessage().equals(expectedLog) && logEvent.getLevel() == Level.ERROR) {
					return;
				}
			}
			Assertions.fail();
		}
		finally {
			log4jInMemoryAppender.removeAppender();
			getTestContainers().getMysqlTestContainer().unpause();
		}
	}
	
	@Test
	void givenSpringContextStarted_whenGetScheduledTasks_thenPrintDataReportInLogsTaskIsScheduled() {
		// Given spring context started
		
		// When
		Optional<CronTask> optionalScheduledTask = getPrintDataReportInLogsScheduledTask();
		
		// Then
		assertTrue(optionalScheduledTask.isPresent());
	}
	
	private Optional<CronTask> getPrintDataReportInLogsScheduledTask() {
		String expectedClassName = PrintDataReportInLogs.class.getName() + "$";
		String expectedMethodName = "scheduledPrintDataReportInLogs";
		
		Set<ScheduledTask> scheduledTasks = scheduledAnnotationBeanPostProcessor.getScheduledTasks();
		for (ScheduledTask scheduledTask : scheduledTasks) {
			CronTask cronTask = (CronTask) scheduledTask.getTask();
			ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) cronTask.getRunnable();
			if (runnable.getMethod().getName().equals(expectedMethodName) && runnable.getTarget().toString().startsWith(expectedClassName)) {
				return Optional.of(cronTask);
			}
		}
		return Optional.empty();
	}
	
	@ParameterizedTest
	@MethodSource("givenValidCurrentDate")
	void givenCronExpression_whenGetScheduledTasks_thenValidScheduling(String currentDateAsString, String nextExpectedTriggerDateAsString) {
		// Given
		Optional<CronTask> optionalScheduledTask = getPrintDataReportInLogsScheduledTask();
		String cronExpressionAsString = optionalScheduledTask.get().getExpression();
		CronExpression cronExpression = CronExpression.parse(cronExpressionAsString);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		// When
		LocalDateTime nextExpectedTriggerDate = cronExpression.next(LocalDateTime.parse(currentDateAsString, dateTimeFormatter));
		
		// Then
		assertEquals(nextExpectedTriggerDateAsString, dateTimeFormatter.format(nextExpectedTriggerDate));
	}
	
	static Stream<Arguments> givenValidCurrentDate() {
		return Stream.of(
			// Current date + next expected trigger date.
			Arguments.of("2022-09-15 08:30:23", "2022-09-15 18:00:00"),
			Arguments.of("2022-09-16 10:05:47", "2022-09-16 18:00:00"),
			Arguments.of("2022-12-31 23:59:59", "2023-01-01 18:00:00"),
			Arguments.of("2050-05-07 18:00:00", "2050-05-08 18:00:00")
		);
	}

}