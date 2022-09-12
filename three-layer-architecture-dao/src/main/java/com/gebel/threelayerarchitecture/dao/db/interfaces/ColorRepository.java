package com.gebel.threelayerarchitecture.dao.db.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gebel.threelayerarchitecture.dao.db.entity.ColorEntity;

public interface ColorRepository extends JpaRepository<ColorEntity, String> {

	Optional<ColorEntity> findOneByHexaCodeIgnoreCase(String hexaCode);
	
}