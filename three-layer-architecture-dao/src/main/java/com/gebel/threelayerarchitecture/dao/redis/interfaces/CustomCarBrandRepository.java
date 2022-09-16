package com.gebel.threelayerarchitecture.dao.redis.interfaces;

import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;

public interface CustomCarBrandRepository {
	
	Iterable<CarBrandModel> findAllWithoutSpringBug();
	
	long countWithoutSpringBug();
	
}