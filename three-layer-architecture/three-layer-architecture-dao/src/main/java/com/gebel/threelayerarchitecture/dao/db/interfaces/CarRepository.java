package com.gebel.threelayerarchitecture.dao.db.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gebel.threelayerarchitecture.dao.db.entity.CarEntity;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, String> {

}