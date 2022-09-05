package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.business.service.converter.DomainColorConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
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

}