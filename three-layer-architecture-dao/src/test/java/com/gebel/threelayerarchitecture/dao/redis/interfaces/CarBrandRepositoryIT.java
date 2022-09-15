package com.gebel.threelayerarchitecture.dao.redis.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.gebel.threelayerarchitecture.dao._test.AbstractIntegrationTest;
import com.gebel.threelayerarchitecture.dao.redis.model.CarBrandModel;

@SpringBootTest
@TestPropertySource("classpath:mysql/application-test-redis.properties")
class CarBrandRepositoryIT extends AbstractIntegrationTest {
	
	@Autowired
	private CarBrandRepository carBrandRepository;
	
	// TODO name
	@Test
//	@Sql("classpath:mysql/car/findById_severalCars.sql")
	void givenSeveralCarBrands_whenFindById_thenOneCarBrandRetrieved() {
		// Given
		String id = "car_brand_id_1";

		// When
		Optional<CarBrandModel> optionalCarModel = carBrandRepository.findById(id);

		// Then
		CarBrandModel foundCarModel = optionalCarModel.get();
		assertEquals("car_brand_id_1", foundCarModel.getId());
		assertEquals("brand_name_1", foundCarModel.getName());
	}

}