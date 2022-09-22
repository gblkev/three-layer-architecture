package com.gebel.threelayerarchitecture.business.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gebel.threelayerarchitecture.business.domain.Brand;
import com.gebel.threelayerarchitecture.business.domain.Model;
import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;
import com.gebel.threelayerarchitecture.dao.redis.model.CarModelModel;

class DomainBrandConverterTest {
	
	private final DomainModelConverter domainModelConverter = new DomainModelConverter();
	private final DomainBrandConverter domainBrandConverter = new DomainBrandConverter(domainModelConverter);
	
	@Test
	void givenOneBrandModel_whenToDomain_thenValidBrandDomain() {
		// Given
		CarModelModel modelModel1 = new CarModelModel("test_model_id1", "model_name1");
		CarModelModel modelModel2 = new CarModelModel("test_model_id2", "model_name2");
		CarBrandModel modelBrand = new CarBrandModel("test_brand_id", "brand_name", List.of(modelModel1, modelModel2));
		
		// When
		Brand domainBrand = domainBrandConverter.toDomain(modelBrand);
		
		// Then
		assertEquals("test_brand_id", domainBrand.getId());
		assertEquals("brand_name", domainBrand.getName());
		
		List<Model> domainModels = domainBrand.getModels();
		Model domainModel1 = domainModels.get(0);
		assertEquals("test_model_id1", domainModel1.getId());
		assertEquals("model_name1", domainModel1.getName());
		Model domainModel2 = domainModels.get(1);
		assertEquals("test_model_id2", domainModel2.getId());
		assertEquals("model_name2", domainModel2.getName());
	}
	
	@Test
	void givenNullBrandModel_whenToDomain_thenNullBrandDomain() {
		// Given
		CarBrandModel modelBrand = null;
		
		// When
		Brand domainBrand = domainBrandConverter.toDomain(modelBrand);
		
		// Then
		assertNull(domainBrand);
	}
	
	@Test
	void givenSeveralBrandModels_whenToDomain_thenValidBrandDomains() {
		// Given
		CarModelModel modelBrand1Model1 = new CarModelModel("test_model_id1", "model_name1");
		CarModelModel modelBrand1Model2 = new CarModelModel("test_model_id2", "model_name2");
		CarBrandModel modelBrand1 = new CarBrandModel("test_brand_id1", "brand_name1", List.of(modelBrand1Model1, modelBrand1Model2));
		
		CarModelModel modelBrand2Model1 = new CarModelModel("test_model_id3", "model_name3");
		CarBrandModel modelBrand2 = new CarBrandModel("test_brand_id2", "brand_name2", List.of(modelBrand2Model1));
		
		List<CarBrandModel> modelsBrands = List.of(modelBrand1, modelBrand2);
		
		// When
		List<Brand> domainBrands = domainBrandConverter.toDomain(modelsBrands);
		
		// Then
		assertEquals(2, domainBrands.size());
		
		Brand domainBrand1 = domainBrands.get(0);
		assertEquals("test_brand_id1", domainBrand1.getId());
		assertEquals("brand_name1", domainBrand1.getName());
		List<Model> domainBrand1Models = domainBrand1.getModels();
		Model domainBrand1Model1 = domainBrand1Models.get(0);
		assertEquals("test_model_id1", domainBrand1Model1.getId());
		assertEquals("model_name1", domainBrand1Model1.getName());
		Model domainBrand1Model2 = domainBrand1Models.get(1);
		assertEquals("test_model_id2", domainBrand1Model2.getId());
		assertEquals("model_name2", domainBrand1Model2.getName());
		
		Brand domainBrand2 = domainBrands.get(1);
		assertEquals("test_brand_id2", domainBrand2.getId());
		assertEquals("brand_name2", domainBrand2.getName());
		List<Model> domainBrand2Models = domainBrand2.getModels();
		Model domainBrand2Model1 = domainBrand2Models.get(0);
		assertEquals("test_model_id3", domainBrand2Model1.getId());
		assertEquals("model_name3", domainBrand2Model1.getName());
	}
	
}