package com.gebel.threelayerarchitecture.business.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gebel.threelayerarchitecture.business.domain.Brand;
import com.gebel.threelayerarchitecture.business.domain.Model;
import com.gebel.threelayerarchitecture.business.service.converter.DomainBrandConverter;
import com.gebel.threelayerarchitecture.business.service.converter.DomainModelConverter;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.dao.redis.interfaces.CustomCarBrandRepository;
import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;
import com.gebel.threelayerarchitecture.dao.redis.model.CarModelModel;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {
	
	@Mock
	private CustomCarBrandRepository customCarBrandRepository;
	
	@Mock
	private ColorService colorService;
	
	private BrandServiceImpl brandService;
	
	@BeforeEach
	void setup() {
		DomainBrandConverter domainBrandConverter = new DomainBrandConverter(new DomainModelConverter());
		brandService = new BrandServiceImpl(customCarBrandRepository, domainBrandConverter);
	}
	
	@Test
	void givenSeveralBrands_whenGetAllBrands_thenAllBrandsRetrieved() {
		// Given
		CarModelModel modelBrand1ModelModel1 = new CarModelModel("model_id1", "model_name1");
		CarModelModel modelBrand1ModelModel2 = new CarModelModel("model_id2", "model_name2");
		CarBrandModel modelBrand1 = new CarBrandModel("brand_id1", "brand_name1", List.of(modelBrand1ModelModel1, modelBrand1ModelModel2));
		
		CarModelModel modelBrand2ModelModel1 = new CarModelModel("model_id3", "model_name3");
		CarBrandModel modelBrand2 = new CarBrandModel("brand_id2", "brand_name2", List.of(modelBrand2ModelModel1));
		
		List<CarBrandModel> modelsBrands = List.of(modelBrand1, modelBrand2);
		when(customCarBrandRepository.findAllWithoutSpringBug())
			.thenReturn(modelsBrands);

		// When
		List<Brand> foundDomainBrands = brandService.getAllBrands();
		
		// Then
		assertEquals(2, foundDomainBrands.size());
		
		Brand foundDomainBrand1 = foundDomainBrands.get(0);
		assertEquals("brand_id1", foundDomainBrand1.getId());
		assertEquals("brand_name1", foundDomainBrand1.getName());
		List<Model> foundDomainBrand1Models = foundDomainBrand1.getModels();
		assertEquals(2, foundDomainBrand1Models.size());
		Model foundDomainBrand1Model1 = foundDomainBrand1Models.get(0);
		assertEquals("model_id1", foundDomainBrand1Model1.getId());
		assertEquals("model_name1", foundDomainBrand1Model1.getName());
		Model foundDomainBrand1Model2 = foundDomainBrand1Models.get(1);
		assertEquals("model_id2", foundDomainBrand1Model2.getId());
		assertEquals("model_name2", foundDomainBrand1Model2.getName());
		
		Brand foundDomainBrand2 = foundDomainBrands.get(1);
		assertEquals("brand_id2", foundDomainBrand2.getId());
		assertEquals("brand_name2", foundDomainBrand2.getName());
		List<Model> foundDomainBrand2Models = foundDomainBrand2.getModels();
		assertEquals(1, foundDomainBrand2Models.size());
		Model foundDomainBrand2Model1 = foundDomainBrand2Models.get(0);
		assertEquals("model_id3", foundDomainBrand2Model1.getId());
		assertEquals("model_name3", foundDomainBrand2Model1.getName());
	}
	
	@Test
	void givenSeveralBrands_whenCountBrands_thenRightCount() {
		// Given
		when(customCarBrandRepository.countWithoutSpringBug())
			.thenReturn(17L);
		
		// When
		long count = brandService.countBrands();
		
		// Then
		assertEquals(17, count);
	}
	
}