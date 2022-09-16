package com.gebel.threelayerarchitecture.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Model {

	private String id;
	private String name;
	
}