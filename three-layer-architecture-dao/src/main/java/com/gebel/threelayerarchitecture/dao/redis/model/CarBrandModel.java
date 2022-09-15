package com.gebel.threelayerarchitecture.dao.redis.model;

import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RedisHash("car_brand")
public class CarBrandModel {

	private String id;
	private String name;
	
}