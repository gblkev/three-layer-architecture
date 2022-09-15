package test.com.gebel.threelayerarchitecture.sandbox.container;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import test.com.gebel.threelayerarchitecture.sandbox.container._test.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class TestContainersRedisIT extends AbstractIntegrationTest {
	
	private static final String REDIS_PASSWORD = "test_password";
	private static final int REDIS_DATABASE = 1;
	
	@Test
	void givenRedisCacheCreated_whenCreatingCarBrands_thenCarBrandsRetrieved() {
		// Given + Redis cache created
		String host = getTestContainersManager().getTestContainers().getRedisTestContainer().getHost();
		int port = getRedisPort();
		RedisURI redisUri = RedisURI.builder()
			.withHost(host)
			.withPort(port)
			.withPassword(REDIS_PASSWORD.toCharArray())
			.withDatabase(REDIS_DATABASE)
			.build();
		
		// When
		createCarBrands(redisUri);
		Map<String, String> createdCarBrands = getAllCarBrands(redisUri);
		
		// Then
		Map<String, String> expectedCarBrands = new HashMap<>();
	    expectedCarBrands.put("car_brand_id1", "car_brand_name1");
	    expectedCarBrands.put("car_brand_id2", "car_brand_name2");
	    
	    assertEquals(expectedCarBrands, createdCarBrands);
	}
	
	private void createCarBrands(RedisURI redisUri) {
		RedisClient redisClient = null;
		try {
			redisClient = RedisClient.create(redisUri);
			StatefulRedisConnection<String, String> connection = redisClient.connect();
			RedisCommands<String, String> commands = connection.sync();
			commands.hset("car_brand", "car_brand_id1", "car_brand_name1");
			commands.hset("car_brand", "car_brand_id2", "car_brand_name2");
		}
		finally {
			redisClient.shutdown();
		}
	}
	
	private Map<String, String> getAllCarBrands(RedisURI redisUri) {
		RedisClient redisClient = null;
		try {
			redisClient = RedisClient.create(redisUri);
			StatefulRedisConnection<String, String> connection = redisClient.connect();
			RedisCommands<String, String> commands = connection.sync();
			return commands.hgetall("car_brand");
		}
		finally {
			redisClient.shutdown();
		}
	}
	
}