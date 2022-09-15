package com.gebel.threelayerarchitecture.dao.redis.model;

import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@RedisHash("car_model")
public class CarModelModel {
	
	private String id;
	private String brandId;
	private String name;

}