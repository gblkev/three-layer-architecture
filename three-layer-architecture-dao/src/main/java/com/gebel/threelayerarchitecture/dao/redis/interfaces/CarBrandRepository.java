package com.gebel.threelayerarchitecture.dao.redis.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;

public interface CarBrandRepository extends JpaRepository<CarBrandModel, String> {

}