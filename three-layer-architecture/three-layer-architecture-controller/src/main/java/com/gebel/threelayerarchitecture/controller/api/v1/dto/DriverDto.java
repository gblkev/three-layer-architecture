package com.gebel.threelayerarchitecture.controller.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverDto {

	@Schema(required = true)
	private String id;
	
	@Schema(required = true)
	private String firstName;
	
	@Schema(required = true)
	private String lastName;
	
}