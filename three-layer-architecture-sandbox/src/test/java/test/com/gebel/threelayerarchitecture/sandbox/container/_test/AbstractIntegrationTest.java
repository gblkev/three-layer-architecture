package test.com.gebel.threelayerarchitecture.sandbox.container._test;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import lombok.Getter;
import test.com.gebel.threelayerarchitecture.sandbox.container.TestContainersManager;

@Getter
public abstract class AbstractIntegrationTest {
	
	private static int MYSQL_PORT;
	private static int REDIS_PORT;
	private static int REST_SERVICES_MOCKSERVER_PORT;
	private static int KAFKA_PORT;
	private static int ZOOKEEPER_PORT;
	
	@Value("${sandbox.mysql.port}")
	private int mysqlPort;
	
	@Value("${sandbox.redis.port}")
	private int redisPort;
	
	@Value("${sandbox.rest.mockserver-port}")
	private int restServicesMockServerPort;
	
	@Value("${sandbox.kafka.port}")
	private int kafkaPort;
	
	@Value("${sandbox.zookeeper.port}")
	private int zookeeperPort;
	
	@SpyBean
	private TestContainersManager testContainersManager;
	
	static {
		allocateFreeTcpPorts();
	}
	
	private static void allocateFreeTcpPorts() {
		MYSQL_PORT = getFreeTcpPort();
		REDIS_PORT = getFreeTcpPort();
		REST_SERVICES_MOCKSERVER_PORT = getFreeTcpPort();
		KAFKA_PORT = getFreeTcpPort();
		ZOOKEEPER_PORT = getFreeTcpPort();
	}
	
	@DynamicPropertySource
	private static void dynamicConfigurationProperties(DynamicPropertyRegistry registry) throws IOException {
		registry.add("sandbox.mysql.port", () -> String.valueOf(MYSQL_PORT));
        registry.add("sandbox.redis.port", () -> String.valueOf(REDIS_PORT));
        registry.add("sandbox.rest.mockserver-port", () -> String.valueOf(REST_SERVICES_MOCKSERVER_PORT));
        registry.add("sandbox.kafka.port", () -> String.valueOf(KAFKA_PORT));
        registry.add("sandbox.zookeeper.port", () -> String.valueOf(ZOOKEEPER_PORT));
	}
	
	protected static int getFreeTcpPort() {
		try (ServerSocket freeServerSocket = new ServerSocket(0)) {
			return freeServerSocket.getLocalPort();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}