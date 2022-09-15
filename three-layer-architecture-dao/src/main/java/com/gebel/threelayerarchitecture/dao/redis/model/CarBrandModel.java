package com.gebel.threelayerarchitecture.dao.redis.model;

import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@RedisHash("car_brand")
public class CarBrandModel {

	private String id;
	private String name;
	
}