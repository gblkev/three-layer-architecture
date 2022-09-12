package com.gebel.threelayerarchitecture.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Car {

	private String id;
	private Color color;
	private Driver driver;
	
}