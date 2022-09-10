package test.com.gebel.threelayerarchitecture.sandbox.container;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class GenericTestContainer {

	private final boolean useRandomPort;
	
	public abstract void start();
	
	public abstract void stop();
	
}