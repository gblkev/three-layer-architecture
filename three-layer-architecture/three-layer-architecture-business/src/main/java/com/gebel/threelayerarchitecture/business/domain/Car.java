package com.gebel.threelayerarchitecture.business.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Car {

	private String id;
	private Color color;
	private Driver driver;
	
}