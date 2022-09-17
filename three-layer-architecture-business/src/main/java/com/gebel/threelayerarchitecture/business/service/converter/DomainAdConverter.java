package com.gebel.threelayerarchitecture.business.service.converter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.Ad;
import com.gebel.threelayerarchitecture.business.domain.AdCategory;
import com.gebel.threelayerarchitecture.dao.rest.dto.FormulaOneAdDto;
import com.gebel.threelayerarchitecture.dao.rest.dto.SportAdDto;

@Component
public class DomainAdConverter {
	
	public Ad formulaOneAdToDomain(FormulaOneAdDto dtoAd) {
		if (dtoAd == null) {
			return null;
		}
		return Ad.builder()
			.id("formulaone-" + dtoAd.getId())
			.category(AdCategory.FORMULA_ONE)
			.message(dtoAd.getMessage())
			.build();
	}

	public List<Ad> formulaOneAdsToDomain(List<FormulaOneAdDto> dtosAds) {
		return CollectionUtils.emptyIfNull(dtosAds)
			.stream()
			.map(this::formulaOneAdToDomain)
			.toList();
	}
	
	public Ad sportAdToDomain(SportAdDto dtoAd) {
		if (dtoAd == null) {
			return null;
		}
		return Ad.builder()
			.id("sport" + dtoAd.getId())
			.category(AdCategory.SPORT)
			.message(dtoAd.getMessage())
			.build();
	}

	public List<Ad> sportAdsToDomain(List<SportAdDto> dtosAds) {
		return CollectionUtils.emptyIfNull(dtosAds)
			.stream()
			.map(this::sportAdToDomain)
			.toList();
	}

}