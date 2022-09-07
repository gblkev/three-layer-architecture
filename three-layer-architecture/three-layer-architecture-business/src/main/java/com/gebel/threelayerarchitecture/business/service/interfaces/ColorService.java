package com.gebel.threelayerarchitecture.business.service.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Color;

public interface ColorService {
	
	List<Color> getAllAvailableColors();
	
	Color createColor(String hexaCode) throws BusinessException;
	
	void deleteColor(String colorId);

}