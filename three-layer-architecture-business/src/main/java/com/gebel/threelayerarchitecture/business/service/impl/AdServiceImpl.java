package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import com.gebel.threelayerarchitecture.business.domain.Ad;
import com.gebel.threelayerarchitecture.business.service.converter.DomainAdConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.AdService;
import com.gebel.threelayerarchitecture.dao.rest.interfaces.FormulaOneAdRestWs;
import com.gebel.threelayerarchitecture.dao.rest.interfaces.SportAdRestWs;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
	
	private final FormulaOneAdRestWs formulaOneAdRestWs;
	private final SportAdRestWs sportAdRestWs;
	private final DomainAdConverter adConverter;

	@Override
	@SneakyThrows({ InterruptedException.class, ExecutionException.class })
	public List<Ad> getPersonalizedAds(String driverId) {
		CompletableFuture<List<Ad>> formulaOneAdsFuture = CompletableFuture.supplyAsync(() -> getFormulaOneAdsSilently(driverId));
		CompletableFuture<List<Ad>> sportAdsFuture = CompletableFuture.supplyAsync(() -> getSportAdsSilently(driverId));
		CompletableFuture.allOf(formulaOneAdsFuture, sportAdsFuture).get();
		return ListUtils.union(formulaOneAdsFuture.get(), sportAdsFuture.get());
	}
	
	private List<Ad> getFormulaOneAdsSilently(String driverId) {
		try {
			return adConverter.formulaOneAdsToDomain(formulaOneAdRestWs.getPersonalizedAds(driverId));
		}
		catch (Exception e) {
			LOGGER.error("Error while retrieving formula one ads for driverId={}", driverId, e);
			return Collections.emptyList();
		}
	}
	
	private List<Ad> getSportAdsSilently(String driverId) {
		try {
			return adConverter.sportAdsToDomain(sportAdRestWs.getPersonalizedAds(driverId));
		}
		catch (Exception e) {
			LOGGER.error("Error while retrieving sport ads for driverId={}", driverId, e);
			return Collections.emptyList();
		}
	}

	@Override
	@SneakyThrows({ InterruptedException.class, ExecutionException.class })
	public void unsubscribePersonalizedAds(String driverId) {
		CompletableFuture<Void> formulaOneAdsFuture = CompletableFuture.runAsync(() -> formulaOneAdRestWs.unsubscribePersonalizedAds(driverId));
		CompletableFuture<Void> sportAdsFuture = CompletableFuture.runAsync(() -> sportAdRestWs.unsubscribePersonalizedAds(driverId));
		CompletableFuture.allOf(formulaOneAdsFuture, sportAdsFuture).get();
	}

}