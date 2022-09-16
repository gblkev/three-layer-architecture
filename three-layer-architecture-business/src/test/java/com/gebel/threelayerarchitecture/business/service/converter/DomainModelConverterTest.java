package com.gebel.threelayerarchitecture.business.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gebel.threelayerarchitecture.business.domain.Model;
import com.gebel.threelayerarchitecture.dao.redis.model.CarModelModel;

class DomainModelConverterTest {

	private final DomainModelConverter domainModelConverter = new DomainModelConverter();
	
	@Test
	void givenOneModelModel_whenToDomain_thenValidModelDomain() {
		// Given
		CarModelModel modelModel = CarModelModel.builder()
			.id("test_id")
			.name("model_name")
			.build();
		
		// When
		Model domainModel = domainModelConverter.toDomain(modelModel);
		
		// Then
		assertEquals("test_id", domainModel.getId());
		assertEquals("model_name", domainModel.getName());
	}
	
	@Test
	void givenNullModelModel_whenToDomain_thenNullModelDomain() {
		// Given
		CarModelModel modelModel = null;
		
		// When
		Model domainModel = domainModelConverter.toDomain(modelModel);
		
		// Then
		assertNull(domainModel);
	}
	
	@Test
	void givenSeveralModelModels_whenToDomain_thenValidModelDomains() {
		// Given
		CarModelModel modelModel1 = CarModelModel.builder()
			.id("test_id1")
			.name("model_name1")
			.build();
		CarModelModel modelModel2 = CarModelModel.builder()
			.id("test_id2")
			.name("model_name2")
			.build();
		List<CarModelModel> modelsModels = List.of(modelModel1, modelModel2);
		
		// When
		List<Model> domainModels = domainModelConverter.toDomain(modelsModels);
		
		// Then
		assertEquals(2, domainModels.size());
		
		Model domainModel1 = domainModels.get(0);
		assertEquals("test_id1", domainModel1.getId());
		assertEquals("model_name1", domainModel1.getName());
		
		Model domainModel2 = domainModels.get(1);
		assertEquals("test_id2", domainModel2.getId());
		assertEquals("model_name2", domainModel2.getName());
	}
	
}