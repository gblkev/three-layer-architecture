package com.gebel.threelayerarchitecture.dao.mysql.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gebel.threelayerarchitecture.dao.mysql.entity.DriverEntity;

public interface DriverRepository extends JpaRepository<DriverEntity, String> {

}