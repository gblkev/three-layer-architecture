package com.gebel.threelayerarchitecture.controller.api.v2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdDto {
	
	private String id;
	private AdCategoryDto category;
	private String message;

}