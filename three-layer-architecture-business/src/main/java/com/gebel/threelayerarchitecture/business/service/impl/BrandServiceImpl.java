package com.gebel.threelayerarchitecture.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gebel.threelayerarchitecture.business.domain.Brand;
import com.gebel.threelayerarchitecture.business.service.converter.DomainBrandConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.BrandService;
import com.gebel.threelayerarchitecture.dao.redis.interfaces.CustomCarBrandRepository;
import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

	private final CustomCarBrandRepository customCarBrandRepository;
	private final DomainBrandConverter domainBrandConverter;
	
	@Override
	@Transactional(readOnly = true)
	public List<Brand> getAllBrands() {
		Iterable<CarBrandModel> carBrandsModels = customCarBrandRepository.findAllWithoutSpringBug();
		List<CarBrandModel> carBrandsModelsAsList = StreamSupport.stream(carBrandsModels.spliterator(), false)
			.collect(Collectors.toList());
		return domainBrandConverter.toDomain(carBrandsModelsAsList);
	}

	@Override
	@Transactional(readOnly = true)
	public long countBrands() {
		return customCarBrandRepository.countWithoutSpringBug();
	}

}