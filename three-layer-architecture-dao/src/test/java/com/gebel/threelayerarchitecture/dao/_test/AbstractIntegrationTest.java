package com.gebel.threelayerarchitecture.dao._test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import test.com.gebel.threelayerarchitecture.sandbox.container.TestContainers;

public abstract class AbstractIntegrationTest {
	
	private static final String MYSQL_DB_NAME = "cars_db";
	private static final String MYSQL_USER = "test_user";
	private static final String MYSQL_PASSWORD = "test_password";
	
	private static final String REDIS_PASSWORD = "test_password";
	private static final int REDIS_DATABASE = 1;
	private static final boolean REDIS_LOAD_INIT_SCRIPT = false;
	
	private static final boolean REST_SERVICES_INIT_MOCKS = false;
	
	private static final TestContainers TEST_CONTAINERS = new TestContainers();
	private static final AtomicBoolean HAS_ALREADY_BEEN_STARTED = new AtomicBoolean(false); 
	
	@AfterEach
	private void clean() {
		TEST_CONTAINERS.resetContainersData();
	}
	
	@DynamicPropertySource
	private static void dynamicConfigurationProperties(DynamicPropertyRegistry registry) throws Exception {
		startContainers();
		setDynamicConfigurationProperties(registry);
	}
	
	protected static TestContainers getTestContainers() {
		return TEST_CONTAINERS;
	}
	
	private static void startContainers() throws Exception {
		if (HAS_ALREADY_BEEN_STARTED.getAndSet(true)) {
			return;
		}
		TEST_CONTAINERS.initMysqlContainerWithRandomPort(MYSQL_DB_NAME, MYSQL_USER, MYSQL_PASSWORD);
		TEST_CONTAINERS.initRedisContainerWithRandomPort(REDIS_PASSWORD, REDIS_DATABASE, REDIS_LOAD_INIT_SCRIPT);
		TEST_CONTAINERS.initRestServicesContainerWithRandomPort(REST_SERVICES_INIT_MOCKS);
		TEST_CONTAINERS.startContainers();
	}
	
	private static void setDynamicConfigurationProperties(DynamicPropertyRegistry registry) throws IOException {
		registry.add("spring.datasource.url", () -> TEST_CONTAINERS.getMysqlTestContainer().getJdbcUrl());
		
		registry.add("spring.redis.host", () -> TEST_CONTAINERS.getRedisTestContainer().getHost());
		registry.add("spring.redis.port", () -> TEST_CONTAINERS.getRedisTestContainer().getPort());
		
		String restServicesContainerHost = TEST_CONTAINERS.getRestServicesTestContainer().getHost();
		int restServicesContainerPort = TEST_CONTAINERS.getRestServicesTestContainer().getPort();
		registry.add("dao.rest.formula-one.ad.url", () -> "http://" + restServicesContainerHost + ":" + restServicesContainerPort + "/formulaone");
		registry.add("dao.rest.sport.ad.url", () -> "http://" + restServicesContainerHost + ":" + restServicesContainerPort + "/sport");
	}
	
}