package com.gebel.threelayerarchitecture.controller.api.v2.interfaces;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.v2.dto.AdDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(V2ApiBaseUri.API_V2_BASE_URI + "/ads")
@Tag(name = "Ads")
public interface AdV2Endpoint {

	@GetMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieve personalized ads for a given driver")
	List<AdDto> getPersonalizedAds(@PathVariable("driverId") String driverId);
	
	@PostMapping(value = "/unsubscribe/{driverId}", consumes = MediaType.TEXT_PLAIN_VALUE)
	@Operation(summary = "Unsubscribe a driver from getting personalized ads")
	void unsubscribePersonalizedAds(@PathVariable("driverId") String driverId);
	
}