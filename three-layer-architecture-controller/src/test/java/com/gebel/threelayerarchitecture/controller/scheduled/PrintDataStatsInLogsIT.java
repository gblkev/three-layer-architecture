package com.gebel.threelayerarchitecture.controller.scheduled;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.gebel.threelayerarchitecture.controller._test.TestContainersManager;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PrintDataStatsInLogsIT {
	
	private static final TestContainersManager TEST_CONTAINERS_MANAGER = new TestContainersManager(); // Shared between all methods.
	
	@DynamicPropertySource
	private static void setupContainersDynamicConfigurationProperties(DynamicPropertyRegistry registry) throws IOException {
		TEST_CONTAINERS_MANAGER.startContainers();
		TEST_CONTAINERS_MANAGER.setDynamicContainersConfiguration(registry);
	}
	
	@AfterAll
	private static void clearAll() {
		TEST_CONTAINERS_MANAGER.stopContainers();
	}
	
	// TODO name
	@Test
	void given_when_then() {
		// Given
		
		// When
		
		// Then
	}

}