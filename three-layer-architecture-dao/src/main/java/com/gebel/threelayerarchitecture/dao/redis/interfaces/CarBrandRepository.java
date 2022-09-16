package com.gebel.threelayerarchitecture.dao.redis.interfaces;

import org.springframework.data.repository.CrudRepository;

import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;

public interface CarBrandRepository extends CrudRepository<CarBrandModel, String> {
	
}