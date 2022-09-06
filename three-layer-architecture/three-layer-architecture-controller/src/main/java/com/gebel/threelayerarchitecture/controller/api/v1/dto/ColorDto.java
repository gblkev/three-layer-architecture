package com.gebel.threelayerarchitecture.controller.api.v1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColorDto {

	private String id;
	private String hexaCode;
	
}