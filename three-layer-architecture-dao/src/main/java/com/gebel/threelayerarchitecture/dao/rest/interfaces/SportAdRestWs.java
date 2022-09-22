package com.gebel.threelayerarchitecture.dao.rest.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.dao.rest.dto.SportAdDto;

public interface SportAdRestWs {

	List<SportAdDto> getPersonalizedAds(String driverId);
	
	void unsubscribePersonalizedAds(String driverId);
	
}