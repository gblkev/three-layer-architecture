package test.com.gebel.threelayerarchitecture.sandbox.container;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.nio.file.Files;
import java.nio.file.Path;

import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import lombok.SneakyThrows;

public class RestServicesTestContainer extends GenericTestContainer<MockServerContainer> {

	private static final int CONTAINER_MAPPED_PORT = MockServerContainer.PORT;
	private static final String INIT_FORMULA_ONE_AD_WS_GET_PERSONALIZED_ADS_JSON_FILE = "rest/init-FormulaOneAdRestWs-getPersonalizedAds.json";
	private static final String INIT_SPORT_AD_WS_GET_PERSONALIZED_ADS_JSON_FILE = "rest/init-SportAdRestWs-getPersonalizedAds.json";
	
	private final boolean initMocks;
	private MockServerClient mockServerClient;
	
	public RestServicesTestContainer(String mockServerDockerImage, boolean initMocks) {
		this(mockServerDockerImage, RANDOM_PORT, initMocks);
	}
	
	public RestServicesTestContainer(String mockServerDockerImage, int mockServerPort, boolean initMocks) {
		super("MockServer (rest web services)");
		this.initMocks = initMocks;
		MockServerContainer container = buildContainer(mockServerDockerImage, mockServerPort, initMocks);
		setContainer(container);
	}
	
	@SuppressWarnings("resource") // Resource closed by "stop()"
	private MockServerContainer buildContainer(String mockServerDockerImage, int mockServerPort, boolean initMocks) {
		MockServerContainer container = new MockServerContainer(DockerImageName.parse(mockServerDockerImage))
			.withExposedPorts(CONTAINER_MAPPED_PORT)
			.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers.rest.mockserver")));
		if (!isRandomPort(mockServerPort)) {
			container.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
				new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(mockServerPort), new ExposedPort(CONTAINER_MAPPED_PORT)))));
		}
		return container;
	}
	
	@Override
	public void start() {
		super.start();
		if (initMocks) {
			initMocks();
		}
	}
	
	@Override
	public void resetContainerData() {
		// Nothing to do
	}
	
	private void initMocks() {
		mockServerClient = new MockServerClient(getHost(), getPort());
		mockFormulaOneAdRestWs();
		mockSportAdRestWs();
	}
	
	@SneakyThrows
	private void mockFormulaOneAdRestWs() {
		mockFormulaOneAdRestWs_GetPersonalizedAds(INIT_FORMULA_ONE_AD_WS_GET_PERSONALIZED_ADS_JSON_FILE);
		mockFormulaOneAdRestWs_Unsubscribe();
	}
	
	public void mockFormulaOneAdRestWs_GetPersonalizedAds(String jsonFile) {
		mockServerClient
			.when(request()
				.withMethod("GET")
				.withPath("/formulaone/([a-zA-Z0-9/-]*)")) // ([a-zA-Z0-9/-]*) for driverId
			.respond(response()
				.withContentType(MediaType.JSON_UTF_8)
				.withBody(readFileFromClasspathAsString(jsonFile)));
	}
	
	private void mockFormulaOneAdRestWs_Unsubscribe() {
		mockServerClient
			.when(request()
				.withMethod("POST")
				.withPath("/formulaone/unsubscribe/([a-zA-Z0-9/-]*)")) // ([a-zA-Z0-9/-]*) for driverId
			.respond(response());
	}
	
	@SneakyThrows
	private void mockSportAdRestWs() {
		mockSportAdRestWs_GetPersonalizedAds(INIT_SPORT_AD_WS_GET_PERSONALIZED_ADS_JSON_FILE);
		mockSportAdRestWs_Unsubscribe();
	}
	
	public void mockSportAdRestWs_GetPersonalizedAds(String jsonFile) {
		mockServerClient
			.when(request()
				.withMethod("GET")
				.withPath("/sport/([a-zA-Z0-9/-]*)")) // ([a-zA-Z0-9/-]*) for driverId
			.respond(response()
				.withContentType(MediaType.JSON_UTF_8)
				.withBody(readFileFromClasspathAsString(jsonFile)));
	}
	
	private void mockSportAdRestWs_Unsubscribe() {
		mockServerClient
			.when(request()
				.withMethod("POST")
				.withPath("/sport/unsubscribe/([a-zA-Z0-9/-]*)")) // ([a-zA-Z0-9/-]*) for driverId
			.respond(response());
	}
	
	@SneakyThrows
	private String readFileFromClasspathAsString(String filePath) {
		Path path = Path.of(ClassLoader.getSystemResource(filePath).toURI());
		return Files.readString(path);
	}

}