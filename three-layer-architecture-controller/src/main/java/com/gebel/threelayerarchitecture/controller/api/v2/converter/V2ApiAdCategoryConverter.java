package com.gebel.threelayerarchitecture.controller.api.v2.converter;

import org.springframework.stereotype.Component;

import com.gebel.threelayerarchitecture.business.domain.AdCategory;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.AdCategoryDto;

@Component
public class V2ApiAdCategoryConverter {

	public AdCategoryDto toDto(AdCategory adCategory) {
		switch (adCategory) {
			case FORMULA_ONE: return AdCategoryDto.FORMULA_ONE;
			case SPORT: return AdCategoryDto.SPORT;
			default: throw new IllegalArgumentException("Category " + adCategory + " is not handled");
		}
	}
	
}