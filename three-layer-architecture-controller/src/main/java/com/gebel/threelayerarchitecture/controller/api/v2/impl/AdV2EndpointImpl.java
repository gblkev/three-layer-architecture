package com.gebel.threelayerarchitecture.controller.api.v2.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.service.interfaces.AdService;
import com.gebel.threelayerarchitecture.controller.api.v2.converter.V2ApiAdConverter;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.AdDto;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.AdV2Endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class AdV2EndpointImpl implements AdV2Endpoint {

	private final AdService adService;
	private final V2ApiAdConverter adConverter;
	
	@Override
	public List<AdDto> getPersonalizedAds(String driverId) {
		LOGGER.info("Retrieving personalized ads for driverId={}", driverId);
		return adConverter.toDto(adService.getPersonalizedAds(driverId));
	}

	@Override
	public void unsubscribePersonalizedAds(String driverId) {
		LOGGER.info("Unsubscribing driverId={} from getting personalized ads", driverId);
		adService.unsubscribePersonalizedAds(driverId);
	}

}