package com.gebel.threelayerarchitecture.business.service.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Color;

public interface ColorService {
	
	Color getColorById(String id);
	
	List<Color> getAllColors();
	
	Color createColor(String hexaCode) throws BusinessException;
	
	void deleteColor(String colorId);

}