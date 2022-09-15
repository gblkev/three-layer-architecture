package com.gebel.threelayerarchitecture.dao.redis.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gebel.threelayerarchitecture.dao.redis.model.CarModelModel;

public interface CarModelRepository extends JpaRepository<CarModelModel, String> {

}