package com.gebel.threelayerarchitecture.business.service.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.business.domain.Ad;

public interface AdService {

	List<Ad> getPersonalizedAds(String driverId);
	
	void unsubscribePersonalizedAds(String driverId);
	
}