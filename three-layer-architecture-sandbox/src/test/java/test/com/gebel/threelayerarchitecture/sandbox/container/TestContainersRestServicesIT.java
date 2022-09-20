package test.com.gebel.threelayerarchitecture.sandbox.container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import test.com.gebel.threelayerarchitecture.sandbox.container._test.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class TestContainersRestServicesIT extends AbstractIntegrationTest {
	
	@Test
	void givenMockedEndpoint_whenCallingEndpoint_thenMessageRetrieved() {
		// Given
		String host = getTestContainersManager().getTestContainers().getRestServicesTestContainer().getHost();
		int port = getTestContainersManager().getTestContainers().getRestServicesTestContainer().getPort();
		String url = "http://" + host + ":" + port + "/test";
		
		// When
		ResponseEntity<String> response;
		try (MockServerClient mockServerClient = new MockServerClient(host, port)) {
			mockServerClient.when(request()
				.withMethod("GET")
				.withPath("/test"))
			.respond(response()
				.withBody("Success!"));
			TestRestTemplate restTemplate = new TestRestTemplate();
			response = restTemplate.getForEntity(url, String.class);
		}
		
		// Then
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals("Success!", response.getBody());
	}
	
}