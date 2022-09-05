package com.gebel.threelayerarchitecture.controller.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColorDto {

	private Long id;
	private String hexaCode;
	
}