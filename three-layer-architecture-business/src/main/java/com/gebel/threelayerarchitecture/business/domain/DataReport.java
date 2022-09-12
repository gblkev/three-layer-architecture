package com.gebel.threelayerarchitecture.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataReport {
	
	private long colorsCount;
	private long driversCount;
	private long carsCount;

}