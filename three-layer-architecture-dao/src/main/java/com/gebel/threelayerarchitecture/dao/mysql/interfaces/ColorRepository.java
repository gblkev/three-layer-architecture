package com.gebel.threelayerarchitecture.dao.mysql.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gebel.threelayerarchitecture.dao.mysql.entity.ColorEntity;

public interface ColorRepository extends JpaRepository<ColorEntity, String> {

	Optional<ColorEntity> findOneByHexaCodeIgnoreCase(String hexaCode);
	
}