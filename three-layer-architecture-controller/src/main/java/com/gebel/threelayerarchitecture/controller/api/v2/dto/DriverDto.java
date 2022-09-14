package com.gebel.threelayerarchitecture.controller.api.v2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {

	@Schema(required = true)
	private String id;
	
	@Schema(required = true, example = "Forrest")
	private String firstName;
	
	@Schema(required = true, example = "Gump")
	private String lastName;
	
}