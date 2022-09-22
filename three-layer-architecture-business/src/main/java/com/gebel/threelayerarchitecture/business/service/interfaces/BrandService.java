package com.gebel.threelayerarchitecture.business.service.interfaces;

import java.util.List;

import com.gebel.threelayerarchitecture.business.domain.Brand;

public interface BrandService {
	
	List<Brand> getAllBrands();
	
	long countBrands();

}