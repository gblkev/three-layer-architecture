package com.gebel.threelayerarchitecture.dao.db.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gebel.threelayerarchitecture.dao.db.entity.CarEntity;

public interface CarRepository extends JpaRepository<CarEntity, String> {

}