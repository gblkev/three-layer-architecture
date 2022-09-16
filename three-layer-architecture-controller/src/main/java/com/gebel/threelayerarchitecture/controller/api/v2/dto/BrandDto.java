package com.gebel.threelayerarchitecture.controller.api.v2.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {
	
	@Schema(required = true)
	private String id;
	
	@Schema(required = true, example = "Ford")
	private String name;
	
	private List<ModelDto> models;

}