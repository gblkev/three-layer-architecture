package com.gebel.threelayerarchitecture.dao.rest.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.dao.rest.dto.FormulaOneAdDto;

public interface FormulaOneAdRestWs {

	List<FormulaOneAdDto> getPersonalizedAds(String driverId);
	
	void unsubscribePersonalizedAds(String driverId);
	
}