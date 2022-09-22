package com.gebel.threelayerarchitecture.dao.mysql.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gebel.threelayerarchitecture.dao.mysql.entity.CarEntity;

public interface CarRepository extends JpaRepository<CarEntity, String> {

}