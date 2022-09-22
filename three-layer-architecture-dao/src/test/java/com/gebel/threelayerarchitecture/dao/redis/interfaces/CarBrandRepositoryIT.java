package com.gebel.threelayerarchitecture.dao.redis.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.gebel.threelayerarchitecture.dao._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;
import com.gebel.threelayerarchitecture.dao.redis.model.CarModelModel;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class CarBrandRepositoryIT extends AbstractIntegrationTest {
	
	@Autowired
	private CarBrandRepository carBrandRepository;
	
	@Autowired
	private CustomCarBrandRepository customCarBrandRepository;
	
	@Test
	void givenSeveralCarBrands_whenFindById_thenOneCarBrandRetrieved() {
		// Given
		getTestContainers().getRedisTestContainer().executeCommandsScript("redis/car_brand/findById_severalCarBrands");
		String id = "car_brand_id_1";
		
		// When
		Optional<CarBrandModel> optionalCarBrandModel = carBrandRepository.findById(id);

		// Then
		CarBrandModel foundCarBrandModel = optionalCarBrandModel.get();
		assertEquals("car_brand_id_1", foundCarBrandModel.getId());
		assertEquals("car_brand_name_1", foundCarBrandModel.getName());
		
		CarModelModel expectedModel1 = new CarModelModel("car_brand_id_1_model_id_1", "car_brand_id_1_model_name_1");
		CarModelModel expectedModel2 = new CarModelModel("car_brand_id_1_model_id_2", "car_brand_id_1_model_name_2");
		List<CarModelModel> expectedModels =  List.of(expectedModel1, expectedModel2);
		assertEquals(expectedModels, foundCarBrandModel.getModels());
	}
	
	@Test
	void givenSeveralCarBrands_whenFindAll_thenAllCarBrandsRetrieved() {
		// Given
		getTestContainers().getRedisTestContainer().executeCommandsScript("redis/car_brand/findAll_severalCarBrands");
		
		// When
		Iterable<CarBrandModel> carBrandsIterable = customCarBrandRepository.findAllWithoutSpringBug();
		Set<CarBrandModel> carBrands = StreamSupport.stream(carBrandsIterable.spliterator(), false)
			.collect(Collectors.toSet());

		// Then
		assertEquals(3, carBrands.size());
		
		CarModelModel carBrand1ExpectedModel1 = new CarModelModel("car_brand_id_1_model_id_1", "car_brand_id_1_model_name_1");
		CarModelModel carBrand1ExpectedModel2 = new CarModelModel("car_brand_id_1_model_id_2", "car_brand_id_1_model_name_2");
		CarBrandModel expectedCarBrand1 = CarBrandModel.builder()
			.id("car_brand_id_1")
			.name("car_brand_name_1")
			.models(List.of(carBrand1ExpectedModel1, carBrand1ExpectedModel2))
			.build();
		
		CarModelModel carBrand2ExpectedModel1 = new CarModelModel("car_brand_id_2_model_id_1", "car_brand_id_2_model_name_1");
		CarBrandModel expectedCarBrand2 = CarBrandModel.builder()
			.id("car_brand_id_2")
			.name("car_brand_name_2")
			.models(List.of(carBrand2ExpectedModel1))
			.build();
		
		CarModelModel carBrand3ExpectedModel1 = new CarModelModel("car_brand_id_3_model_id_1", "car_brand_id_3_model_name_1");
		CarModelModel carBrand3ExpectedModel2 = new CarModelModel("car_brand_id_3_model_id_2", "car_brand_id_3_model_name_2");
		CarModelModel carBrand3ExpectedModel3 = new CarModelModel("car_brand_id_3_model_id_3", "car_brand_id_3_model_name_3");
		CarModelModel carBrand3ExpectedModel4 = new CarModelModel("car_brand_id_3_model_id_4", "car_brand_id_3_model_name_4");
		CarBrandModel expectedCarBrand3 = CarBrandModel.builder()
			.id("car_brand_id_3")
			.name("car_brand_name_3")
			.models(List.of(carBrand3ExpectedModel1, carBrand3ExpectedModel2, carBrand3ExpectedModel3, carBrand3ExpectedModel4))
			.build();
		Set<CarBrandModel> expectedCarBrands = Set.of(expectedCarBrand1, expectedCarBrand2, expectedCarBrand3);
		assertEquals(expectedCarBrands, carBrands);
	}
	
	@Test
	void givenSeveralCarBrands_whenCount_thenRightCount() {
		// Given
		getTestContainers().getRedisTestContainer().executeCommandsScript("redis/car_brand/count_severalCarBrands");
		
		// When
		long count = customCarBrandRepository.countWithoutSpringBug();

		// Then
		assertEquals(3, count);
	}
	
}