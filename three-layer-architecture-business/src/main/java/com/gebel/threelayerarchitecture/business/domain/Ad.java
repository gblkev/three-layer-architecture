package com.gebel.threelayerarchitecture.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Ad {
	
	private String id;
	private AdCategory category;
	private String message;

}