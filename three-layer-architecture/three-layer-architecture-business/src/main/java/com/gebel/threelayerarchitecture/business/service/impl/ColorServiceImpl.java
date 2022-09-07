package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gebel.threelayerarchitecture.business.domain.BusinessErrorCode;
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
	@Transactional(readOnly = true)
	public List<Color> getAllAvailableColors() {
		return colorConverter.toDomain(colorRepository.findAll());
	}

	@Override
	@Transactional
	public Color createColor(String hexaCode) throws BusinessException {
		Color color = Color.builder()
			.hexaCode(hexaCode)
			.build();
		checkColorCreationPrerequisites(color);
		return createColorInDb(color);
	}
	
	private void checkColorCreationPrerequisites(Color color) throws BusinessException {
		color.validate();
		checkIfColorAlreadyExists(color);
	}
	
	private void checkIfColorAlreadyExists(Color color) throws BusinessException {
		boolean doesColorAlreadyExist = colorRepository.findOneByHexaCodeIgnoreCase(color.getHexaCode()).isPresent();
		if (doesColorAlreadyExist) {
			throw new BusinessException("Color " + color.getHexaCode() + " already exists", BusinessErrorCode.COLOR_SAME_HEXA_CODE_ALREADY_EXISTS);
		}
	}
	
	private Color createColorInDb(Color color) {
		ColorEntity colorEntity = colorConverter.toEntity(color);
		colorRepository.save(colorEntity);
		return colorConverter.toDomain(colorEntity);
	}

	@Override
	@Transactional
	public void deleteColor(String colorId) {
		if (colorRepository.existsById(colorId)) {
			colorRepository.deleteById(colorId);
		}
	}
	
}