package com.gebel.threelayerarchitecture.controller.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDriverDto {
	
	@Schema(required = true, example = "Forrest", pattern = "Between 1 and 30 characters, any character")
	private String firstName;
	
	@Schema(required = true, example = "Gump", pattern = "Between 1 and 30 characters, any character")
	private String lastName;

}