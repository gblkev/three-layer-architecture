package com.gebel.threelayerarchitecture.controller._test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import lombok.Getter;
import test.com.gebel.threelayerarchitecture.sandbox.container.TestContainers;

/**
 * Holds everything which is shared between integration tests.
 * 
 * If different beans are injected in 2 given test classes, Spring creates a new context for each of them.
 * In order to use the same context for all classes (and save some time), we inject in this class the beans we need to use.
 */
public abstract class AbstractIntegrationTest {
	
	private static final String MYSQL_DOCKER_IMAGE = "mysql:8.0.11";
	private static final String MYSQL_DB_NAME = "cars_db";
	private static final String MYSQL_USER = "test_user";
	private static final String MYSQL_PASSWORD = "test_password";
	
	private static final String REDIS_DOCKER_IMAGE = "redis:7.0.4";
	private static final String REDIS_PASSWORD = "test_password";
	private static final int REDIS_DATABASE = 1;
	private static final boolean REDIS_LOAD_INIT_SCRIPT = false;
	
	private static final String REST_SERVICES_MOCKSERVER_DOCKER_IMAGE = "jamesdbloom/mockserver:mockserver-5.13.2";
	private static final boolean REST_SERVICES_INIT_MOCKS = false;
	
	private static final String KAFKA_DOCKER_IMAGE = "confluentinc/cp-kafka:7.2.1";
	private static final List<String> KAFKA_TOPICS = List.of("threelayerarchitecture.create-color");

	private static final TestContainers TEST_CONTAINERS = new TestContainers();
	private static final AtomicBoolean HAS_ALREADY_BEEN_STARTED = new AtomicBoolean(false); 
	
	@Getter
	@LocalServerPort
	private int serverPort;
	
	@Getter
	@LocalManagementPort
	private int managementPort;
	
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
		TEST_CONTAINERS.initMysqlContainerWithRandomPort(MYSQL_DOCKER_IMAGE, MYSQL_DB_NAME, MYSQL_USER, MYSQL_PASSWORD);
		TEST_CONTAINERS.initRedisContainerWithRandomPort(REDIS_DOCKER_IMAGE, REDIS_PASSWORD, REDIS_DATABASE, REDIS_LOAD_INIT_SCRIPT);
		TEST_CONTAINERS.initRestServicesContainerWithRandomPort(REST_SERVICES_MOCKSERVER_DOCKER_IMAGE, REST_SERVICES_INIT_MOCKS);
		TEST_CONTAINERS.initZookeeperKafkaContainersWithRandomPort(KAFKA_DOCKER_IMAGE, KAFKA_TOPICS);
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
		
		registry.add("spring.kafka.properties.bootstrap.servers", () -> TEST_CONTAINERS.getZookeeperKafkaTestContainers().getContainer().getBootstrapServers());
	}
	
}