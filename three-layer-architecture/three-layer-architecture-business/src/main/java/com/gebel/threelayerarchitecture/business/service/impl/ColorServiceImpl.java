package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.business.service.converter.DomainColorConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.dao.db.entity.ColorEntity;
import com.gebel.threelayerarchitecture.dao.db.interfaces.ColorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ColorServiceImpl implements ColorService {

	private final ColorRepository colorRepository;
	private final DomainColorConverter colorConverter;
	
	@Override
	public List<Color> getAllAvailableColors() {
		return colorConverter.toDomain(colorRepository.findAll());
	}

	@Override
	@Transactional
	public Color createColor(String hexaCode) throws BusinessException {
		Color color = Color.builder()
			.hexaCode(hexaCode)
			.build();
		color.validate();
		
		ColorEntity colorEntity = colorConverter.toEntity(color);
		colorRepository.save(colorEntity);
		
		return colorConverter.toDomain(colorEntity);
	}

	@Override
	public void deleteColor(String id) {
		colorRepository.deleteById(id);
	}
	
}