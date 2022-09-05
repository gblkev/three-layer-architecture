package com.gebel.threelayerarchitecture.dao.db.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gebel.threelayerarchitecture.dao.db.entity.ColorEntity;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Integer> {

}