package com.gebel.threelayerarchitecture.dao.rest.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.gebel.threelayerarchitecture.dao.rest.dto.SportAdDto;
import com.gebel.threelayerarchitecture.dao.rest.interfaces.SportAdRestWs;

@Component
public class SportAdRestWsImpl implements SportAdRestWs {

	private static final String DRIVER_ID_URI_PARAMETER = "driverId";
	
	@Value("${dao.rest.sport.ad.url}")
	private String baseUrl;

	private UriComponents getPersonalizedAdUrlTemplate;
	private UriComponents unsubscribePersonalizedAdUrlTemplate;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@PostConstruct
	private void init() {
		getPersonalizedAdUrlTemplate = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/{" + DRIVER_ID_URI_PARAMETER + "}")
			.build();
		unsubscribePersonalizedAdUrlTemplate = UriComponentsBuilder.fromUriString(baseUrl)
			.path("/unsubscribe/{" + DRIVER_ID_URI_PARAMETER + "}")
			.build();
	}
	
	@Override
	public List<SportAdDto> getPersonalizedAds(String driverId) {
		String getPersonalizedAdUrl = getPersonalizedAdUrlTemplate
			.expand(Map.of(DRIVER_ID_URI_PARAMETER, driverId))
			.toUriString();
		try {
			return Arrays.asList(restTemplate.getForObject(getPersonalizedAdUrl, SportAdDto[].class, driverId));
		}
		catch (Exception e) {
			String message = String.format(
				"Error while calling SportAdRestWs.getPersonalizedAd for driverId=%s and url=%s",
				driverId,
				getPersonalizedAdUrl);
			throw new RuntimeException(message, e);
		}
	}

	@Override
	public void unsubscribePersonalizedAds(String driverId) {
		String unsubscribePersonalizedAdUrl = unsubscribePersonalizedAdUrlTemplate
			.expand(Map.of(DRIVER_ID_URI_PARAMETER, driverId))
			.toUriString();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			restTemplate.postForEntity(unsubscribePersonalizedAdUrl, httpEntity, Void.class, driverId);
		}
		catch (Exception e) {
			String message = String.format(
				"Error while calling SportAdRestWs.unsubscribePersonalizedAd for driverId=%s and url=%s",
				driverId,
				unsubscribePersonalizedAdUrl);
			throw new RuntimeException(message, e);
		}
	}
	
}