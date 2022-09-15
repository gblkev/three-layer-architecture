package com.gebel.threelayerarchitecture.dao.redis.interfaces;

import org.springframework.data.repository.CrudRepository;

import com.gebel.threelayerarchitecture.dao.redis.model.CarModelModel;

public interface CarModelRepository extends CrudRepository<CarModelModel, String> {

}