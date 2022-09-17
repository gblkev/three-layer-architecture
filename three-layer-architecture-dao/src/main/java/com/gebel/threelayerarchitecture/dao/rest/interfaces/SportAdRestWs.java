package com.gebel.threelayerarchitecture.dao.rest.interfaces;

import com.gebel.threelayerarchitecture.dao.rest.dto.SportAdDto;

public interface SportAdRestWs {

	SportAdDto getPersonalizedAd(String driverId);
	
	void unsubscribePersonalizedAd(String driverId);
	
}