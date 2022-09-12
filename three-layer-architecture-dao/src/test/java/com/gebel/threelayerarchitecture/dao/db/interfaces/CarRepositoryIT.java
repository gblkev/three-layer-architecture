package com.gebel.threelayerarchitecture.dao.db.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.gebel.threelayerarchitecture.dao.db.entity.CarEntity;
import com.gebel.threelayerarchitecture.dao.db.entity.ColorEntity;
import com.gebel.threelayerarchitecture.dao.db.entity.DriverEntity;

import test.com.gebel.threelayerarchitecture.sandbox.container.MysqlDatabaseTestContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource("classpath:db/application-test-mysql.properties")
class CarRepositoryIT {

	// Ex: c2bba799-02db-4b4b-8782-0df1517bbe1d
	private static final String UUID_REGEX = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
	
	// Shared between all methods.
	private static final MysqlDatabaseTestContainer MYSQL_CONTAINER = new MysqlDatabaseTestContainer("8.0.11", "cars_db", "test_user", "test_password");

	@Autowired
	private ColorRepository colorRepository;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private CarRepository carRepository;

	@DynamicPropertySource
	private static void mysqlDynamicConfigurationProperties(DynamicPropertyRegistry registry) throws IOException {
		MYSQL_CONTAINER.start();
		registry.add("spring.datasource.url", () -> MYSQL_CONTAINER.getJdbcUrl());
	}

	@AfterAll
	private static void clearAll() {
		MYSQL_CONTAINER.stop();
	}

	@Test
	@Sql("classpath:db/car/findById_severalCars.sql")
	void givenSeveralCars_whenFindById_thenOneCarRetrieved() {
		// Given + sql
		String id = "car_id_1";

		// When
		Optional<CarEntity> optionalCar = carRepository.findById(id);

		// Then
		CarEntity foundCar = optionalCar.get();
		assertEquals("car_id_1", foundCar.getId());
		
		ColorEntity color = foundCar.getColor();
		assertEquals("color_id_1", color.getId());
		assertEquals("#000000", color.getHexaCode());
		
		DriverEntity driver = foundCar.getDriver();
		assertEquals("driver_id_1", driver.getId());
		assertEquals("Forrest", driver.getFirstName());
		assertEquals("Gump", driver.getLastName());
	}

	@Test
	@Sql("classpath:db/car/findAll_severalCars.sql")
	void givenSeveralCars_whenFindAll_thenAllCarsRetrieved() {
		// Given sql

		// When
		List<CarEntity> cars = carRepository.findAll();

		// Then
		assertEquals(3, cars.size());

		CarEntity car1 = cars.get(0);
		assertEquals("car_id_1", car1.getId());
		ColorEntity color1 = car1.getColor();
		assertEquals("color_id_1", color1.getId());
		assertEquals("#000000", color1.getHexaCode());
		DriverEntity driver1 = car1.getDriver();
		assertEquals("driver_id_1", driver1.getId());
		assertEquals("Forrest", driver1.getFirstName());
		assertEquals("Gump", driver1.getLastName());

		CarEntity car2 = cars.get(1);
		assertEquals("car_id_2", car2.getId());
		ColorEntity color2 = car2.getColor();
		assertEquals("color_id_2", color2.getId());
		assertEquals("#000001", color2.getHexaCode());
		DriverEntity driver2 = car2.getDriver();
		assertEquals("driver_id_2", driver2.getId());
		assertEquals("Tom", driver2.getFirstName());
		assertEquals("Hanks", driver2.getLastName());

		CarEntity car3 = cars.get(2);
		assertEquals("car_id_3", car3.getId());
		ColorEntity color3 = car3.getColor();
		assertEquals("color_id_3", color3.getId());
		assertEquals("#000002", color3.getHexaCode());
		DriverEntity driver3 = car3.getDriver();
		assertEquals("driver_id_3", driver3.getId());
		assertEquals("Robert", driver3.getFirstName());
		assertEquals("Zemeckis", driver3.getLastName());
	}
	
	@Test
	@Sql("classpath:db/car/create_existingColorAndDriver.sql")
	void givenNoCars_whenSave_thenCarCreated() {
		// Given + empty table
		ColorEntity colorOfCarToCreate = colorRepository.findById("color_id_1").get();
		DriverEntity driverOfCarToCreate = driverRepository.findById("driver_id_1").get();
		CarEntity carToCreate = CarEntity.builder()
			.id(null)
			.color(colorOfCarToCreate)
			.driver(driverOfCarToCreate)
			.build();

		// When
		CarEntity createdCar = carRepository.save(carToCreate);

		// Then
		assertEquals(1, carRepository.count());
		assertIdFormat(createdCar.getId());
		
		ColorEntity createdCarColor = createdCar.getColor();
		assertEquals("color_id_1", createdCarColor.getId());
		assertEquals("#000000", createdCarColor.getHexaCode());
		
		DriverEntity createdCarDriver = createdCar.getDriver();
		assertEquals("driver_id_1", createdCarDriver.getId());
		assertEquals("Forrest", createdCarDriver.getFirstName());
		assertEquals("Gump", createdCarDriver.getLastName());
	}
	
	private void assertIdFormat(String id) {
		Pattern pattern = Pattern.compile(UUID_REGEX);
		assertTrue(pattern.matcher(id).matches());
	}
	
	@Test
	@Sql("classpath:db/car/deleteById_severalCars.sql")
	void givenSeveralCars_whenDeleteById_thenCarDeleted() {
		// Given + sql
		String id = "car_id_1";
		assertTrue(carRepository.findById(id).isPresent());
		assertEquals(3, carRepository.count());

		// When
		carRepository.deleteById(id);

		// Then
		assertFalse(carRepository.findById(id).isPresent());
		assertEquals(2, carRepository.count());
	}

}