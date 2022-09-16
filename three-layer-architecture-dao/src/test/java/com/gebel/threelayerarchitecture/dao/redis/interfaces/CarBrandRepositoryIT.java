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

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class CarBrandRepositoryIT extends AbstractIntegrationTest {
	
	@Autowired
	private CarBrandRepository carBrandRepository;
	
	@Autowired
	private CustomCarBrandRepository customCarBrandRepository;
	
	@Test
	void givenSeveralCarBrands_whenFindById_thenOneCarBrandRetrieved() throws Exception {
		// Given
		getTestContainers().getRedisTestContainer().populateRedisCache("redis/car_brand/findById_severalCarBrands", getRedisDatabase());
		String id = "car_brand_id_1";
		
		// When
		Optional<CarBrandModel> optionalCarBrandModel = carBrandRepository.findById(id);

		// Then
		CarBrandModel foundCarBrandModel = optionalCarBrandModel.get();
		assertEquals("car_brand_id_1", foundCarBrandModel.getId());
		assertEquals("car_brand_name_1", foundCarBrandModel.getName());
		List<String> expectedModels =  List.of(
			"car_brand_id_1_model_1",
			"car_brand_id_1_model_2");
		assertEquals(expectedModels, foundCarBrandModel.getModels());
	}
	
	@Test
	void givenSeveralCarBrands_whenFindAll_thenAllCarBrandsRetrieved() throws Exception {
		// Given
		getTestContainers().getRedisTestContainer().populateRedisCache("redis/car_brand/findAll_severalCarBrands", getRedisDatabase());

		// When
		Iterable<CarBrandModel> carBrandsIterable = customCarBrandRepository.findAllWithoutSpringBug();
		Set<CarBrandModel> carBrands = StreamSupport.stream(carBrandsIterable.spliterator(), false)
			.collect(Collectors.toSet());

		// Then
		assertEquals(3, carBrands.size());
		
		CarBrandModel expectedCarBrand1 = CarBrandModel.builder()
			.id("car_brand_id_1")
			.name("car_brand_name_1")
			.models(List.of("car_brand_id_1_model_1", "car_brand_id_1_model_2"))
			.build();
		CarBrandModel expectedCarBrand2 = CarBrandModel.builder()
			.id("car_brand_id_2")
			.name("car_brand_name_2")
			.models(List.of("car_brand_id_2_model_1"))
			.build();
		CarBrandModel expectedCarBrand3 = CarBrandModel.builder()
			.id("car_brand_id_3")
			.name("car_brand_name_3")
			.models(List.of("car_brand_id_3_model_1", "car_brand_id_3_model_2", "car_brand_id_3_model_3", "car_brand_id_3_model_4"))
			.build();
		Set<CarBrandModel> expectedCarBrands = Set.of(expectedCarBrand1, expectedCarBrand2, expectedCarBrand3);
		assertEquals(expectedCarBrands, carBrands);
	}
	
}