package com.gebel.threelayerarchitecture.dao.db.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gebel.threelayerarchitecture.dao.db.entity.DriverEntity;

public interface DriverRepository extends JpaRepository<DriverEntity, String> {

}