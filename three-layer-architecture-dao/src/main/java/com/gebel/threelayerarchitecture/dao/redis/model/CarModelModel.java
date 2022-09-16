package com.gebel.threelayerarchitecture.dao.redis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CarModelModel {

	private String id;
	private String name;
	
}