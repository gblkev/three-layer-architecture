package test.com.gebel.threelayerarchitecture.sandbox.container;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class TestContainersManager {
	
	private final TestContainersFactory testContainersFactory;
	
	private TestContainers testContainers;
	
	@PostConstruct
	public void startContainers() throws Exception {
		testContainers = testContainersFactory.build();
		testContainers.startContainers();
	}
	
	@PreDestroy
	public void stopContainers() {
		testContainers.stopContainers();
	}

}