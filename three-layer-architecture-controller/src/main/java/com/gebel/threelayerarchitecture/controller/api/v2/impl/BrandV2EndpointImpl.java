package com.gebel.threelayerarchitecture.controller.api.v2.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.gebel.threelayerarchitecture.business.service.interfaces.BrandService;
import com.gebel.threelayerarchitecture.controller.api.v2.converter.V2ApiBrandConverter;
import com.gebel.threelayerarchitecture.controller.api.v2.dto.BrandDto;
import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.BrandV2Endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class BrandV2EndpointImpl implements BrandV2Endpoint {

	private final BrandService brandService;
	private final V2ApiBrandConverter brandConverter;
	
	@Override
	public List<BrandDto> getAllBrands() {
		LOGGER.info("Listing all brands");
		return brandConverter.toDto(brandService.getAllBrands());
	}

}