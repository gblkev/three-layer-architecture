package com.gebel.threelayerarchitecture.business.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Brand {

	private String id;
	private String name;
	private List<Model> models;
	
}