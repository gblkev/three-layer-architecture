package com.gebel.threelayerarchitecture.controller.scheduled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.test.context.jdbc.Sql;

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
	@Sql("classpath:scheduled/printDataReportInLogs/createSeveralCars.sql")
	void givenDataReportGenerated_whenScheduledPrintDataReportInLogs_thenDataReportPrintedOutInLogs() {
		Log4jInMemoryAppender log4jInMemoryAppender = new Log4jInMemoryAppender();
		try {
			// Given + sql
			log4jInMemoryAppender.setupAppender(PrintDataReportInLogs.class);
			
			// When
			printDataReportInLogs.scheduledPrintDataReportInLogs();
			
			// Then
			List<String> expectedLogs = List.of(
				"#######################",
				"Data report:",
				"    Colors in db: 4",
				"    Drivers in db: 2",
				"    Cars in db: 6",
				"#######################");
			List<String> eventsMessagesAsString = log4jInMemoryAppender.getEventsMessagesAsString();
			assertEquals(expectedLogs, eventsMessagesAsString);
		}
		finally {
			log4jInMemoryAppender.removeAppender(PrintDataReportInLogs.class);
		}
	}
	
	@Test
	void givenDatabaseUnavailable_whenScheduledPrintDataReportInLogs_thenErrorInLogs() {
		Log4jInMemoryAppender log4jInMemoryAppender = new Log4jInMemoryAppender();
		try {
			// Given + sql
			log4jInMemoryAppender.setupAppender(PrintDataReportInLogs.class);
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
			log4jInMemoryAppender.removeAppender(PrintDataReportInLogs.class);
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
	
	// TODO ParameterizedTest
	@Test
	void givenCronExpression_whenGetScheduledTasks_thenValidScheduling() {
		// Given
		Optional<CronTask> optionalScheduledTask = getPrintDataReportInLogsScheduledTask();
		String cronExpressionAsString = optionalScheduledTask.get().getExpression();
		CronExpression cronExpression = CronExpression.parse(cronExpressionAsString);
		
		// When
		LocalDateTime next = cronExpression.next(LocalDateTime.now());
		
		// Then
		
	}

}