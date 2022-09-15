package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisTestContainer extends GenericTestContainer<GenericContainer<?>> {

	private static final int CONTAINER_MAPPED_PORT = 6379;
	
	private final String redisPassword;

	public RedisTestContainer(String redisDockerImage, String redisPassword) {
		this(redisDockerImage, RANDOM_PORT, redisPassword);
	}
	
	public RedisTestContainer(String redisDockerImage, int redisPort, String redisPassword) {
		super("Redis");
		this.redisPassword = redisPassword;
		GenericContainer<?> container = buildContainer(redisDockerImage, redisPort, redisPassword);
		setContainer(container);
	}

	@SuppressWarnings("resource") // Resource closed by "stop()"
	private GenericContainer<?> buildContainer(String redisDockerImage, int redisPort, String redisPassword) {
		GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse(redisDockerImage))
			.withExposedPorts(CONTAINER_MAPPED_PORT)
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.redis")))
			.withCommand("redis-server", "--requirepass", redisPassword);
		if (!isRandomPort(redisPort)) {
			container.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
				new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(redisPort), new ExposedPort(CONTAINER_MAPPED_PORT)))));
		}
		return container;
	}
	
	@Override
	public void resetContainerData() {
		LOGGER.info("Resetting Redis data...");
		RedisURI redisUri = buildRedisUri();
		flushAll(redisUri);
	}
	
	private RedisURI buildRedisUri() {
		return RedisURI.builder()
			.withHost(getHost())
			.withPort(getPort())
			.withPassword(redisPassword.toCharArray())
			.build();
	}
	
	private void flushAll(RedisURI redisUri) {
		RedisClient redisClient = null;
		try {
			redisClient = RedisClient.create(redisUri);
			StatefulRedisConnection<String, String> connection = redisClient.connect();
			RedisCommands<String, String> commands = connection.sync();
			commands.flushall();
		}
		finally {
			redisClient.shutdown();
		}
	}
	
	public void populateRedisCache(String redisHash, String jsonFilePath, int database) throws Exception {
		RedisURI redisUri = buildRedisUri(database);
		RedisClient redisClient = null;
		try {
			redisClient = RedisClient.create(redisUri);
			StatefulRedisConnection<String, String> connection = redisClient.connect();
			RedisCommands<String, String> commands = connection.sync();
			List<JsonNode> jsonNodes = getJsonNodes(jsonFilePath);
			jsonNodes.stream()
				.forEach(jsonNode -> this.persistJsonNodeToRedis(commands, redisHash, jsonNode));
		}
		finally {
			redisClient.shutdown();
		}
	}
	
	private RedisURI buildRedisUri(int database) {
		return RedisURI.builder()
			.withHost(getHost())
			.withPort(getPort())
			.withPassword(redisPassword.toCharArray())
			.withDatabase(database)
			.build();
	}
	
	private List<JsonNode> getJsonNodes(String jsonFilePath) throws IOException {
		InputStream jsonFileAsStream = RedisTestContainer.class.getClassLoader().getResourceAsStream(jsonFilePath);
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode rootNode = (ArrayNode) objectMapper.readTree(jsonFileAsStream);
		
		List<JsonNode> jsonNodes = new ArrayList<>();
		rootNode.elements()
			.forEachRemaining(jsonNodes::add);
		return jsonNodes;
	}
	
	private void persistJsonNodeToRedis(RedisCommands<String, String> commands, String redisHash, JsonNode jsonNode) {
		String id = jsonNode.get("id").asText();
		String hashKey = redisHash + ":" + id;
		jsonNode.fields().forEachRemaining(field -> {
			String fieldName = field.getKey();
			String value = field.getValue().asText();
			commands.hset(hashKey, fieldName, value);
		});
		commands.sadd(redisHash, hashKey);
	}
	
}