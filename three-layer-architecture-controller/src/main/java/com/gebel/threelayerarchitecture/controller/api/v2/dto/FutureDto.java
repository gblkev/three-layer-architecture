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
public class FutureDto {

	@Schema(required = true)
	private String message;
	
}