package com.gebel.threelayerarchitecture.dao.rest.interfaces;

import com.gebel.threelayerarchitecture.dao.rest.dto.FormulaOneAdDto;

public interface FormulaOneAdRestWs {

	FormulaOneAdDto getPersonalizedAd(String driverId);
	
	void unsubscribePersonalizedAd(String driverId);
	
}