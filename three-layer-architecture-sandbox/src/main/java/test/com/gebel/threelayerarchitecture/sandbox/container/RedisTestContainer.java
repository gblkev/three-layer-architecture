package test.com.gebel.threelayerarchitecture.sandbox.container;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;
import org.testcontainers.utility.DockerImageName;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisTestContainer extends GenericTestContainer<GenericContainer<?>> {

	private static final int CONTAINER_MAPPED_PORT = 6379;
	private static final String INIT_SCRIPT_PATH = "redis/redis-init-data";
	
	private final String redisPassword;
	private final int redisDatabase;
	private final boolean loadInitScript;

	public RedisTestContainer(String redisDockerImage, String redisPassword, int redisDatabase, boolean loadInitScript) throws Exception {
		this(redisDockerImage, RANDOM_PORT, redisPassword, redisDatabase, loadInitScript);
	}
	
	public RedisTestContainer(String redisDockerImage, int redisPort, String redisPassword, int redisDatabase, boolean loadInitScript) throws Exception {
		super("Redis");
		this.redisPassword = redisPassword;
		this.redisDatabase = redisDatabase;
		this.loadInitScript = loadInitScript;
		GenericContainer<?> container = buildContainer(redisDockerImage, redisPort, redisPassword, redisDatabase, loadInitScript);
		setContainer(container);
	}

	@SuppressWarnings("resource") // Resource closed by "stop()"
	private GenericContainer<?> buildContainer(String redisDockerImage, int redisPort, String redisPassword, int redisDatabase, boolean loadInitScript) throws Exception {
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
	public void start() {
		super.start();
		if (loadInitScript) {
			loadInitScriptSilently();
		}
	}
	
	private void loadInitScriptSilently() {
		try {
			executeCommandsScript(INIT_SCRIPT_PATH);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
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
			.withDatabase(redisDatabase)
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
	
	// I know, it's not very clean but I can't find any other solution right now :/
	@SneakyThrows
	public void executeCommandsScript(String commandsScriptPath) {
		RedisClient redisClient = null;
		try {
			RedisURI redisUri = buildRedisUri();
			redisClient = RedisClient.create(redisUri);
			StatefulRedisConnection<String, String> connection = redisClient.connect();
			RedisCommands<String, String> commands = connection.sync();
			URI uri = RedisTestContainer.class.getClassLoader().getResource(commandsScriptPath).toURI();
			Files.readAllLines(Paths.get(uri))
				.forEach(command -> executeCommandSilently(command, commands));
		}
		finally {
			if (redisClient != null) {
				redisClient.shutdown();
			}
		}
	}
	
	private void executeCommandSilently(String commandAsString, RedisCommands<String, String> commands) {
		try {
			executeCommand(commandAsString, commands);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void executeCommand(String commandAsString, RedisCommands<String, String> commands) {
		if (StringUtils.isBlank(commandAsString)) {
			return;
		}
		String methodToCallAsString = commandAsString.split(" ")[0].toLowerCase();
		String[] arguments = splitArguments(commandAsString);
		switch (methodToCallAsString) {
			case "hset": hset(arguments, commands); return;
			case "sadd": sadd(arguments, commands); return;
			default: throw new IllegalArgumentException("Command " + methodToCallAsString + " is not implemented yet");
		}
	}
	
	private String[] splitArguments(String s) {
		return Pattern.compile("\"(.*?)\"")
	    	.matcher(s)
	    	.results()
	    	.map(MatchResult::group)
	    	.map(this::cleanArgument)
	    	.toArray(size -> new String[size]);
	}
	
	private String cleanArgument(String argument) {
		if (argument.startsWith("\"") && argument.endsWith("\"")) {
			return argument.substring(1, argument.length() - 1);
		}
		return argument;
	}
	
	private void hset(String[] arguments, RedisCommands<String, String> commands) {
		commands.hset(arguments[0], arguments[1], arguments[2]);
	}
	
	private void sadd(String[] arguments, RedisCommands<String, String> commands) {
		String firstArgument = arguments[0];
		String[] allArgumentsAfterTheFirst = Arrays.copyOfRange(arguments, 1, arguments.length);
		commands.sadd(firstArgument, allArgumentsAfterTheFirst);
	}
	
}