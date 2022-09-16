package com.gebel.threelayerarchitecture.dao.redis.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RedisHash("car_brand")
public class CarBrandModel {

	@Id
	private String id;
	
	private String name;
	
	private List<String> models;
	
}