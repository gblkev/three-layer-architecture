package com.gebel.threelayerarchitecture.business.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Color {
	
	private Long id;
	private String hexaCode;

}