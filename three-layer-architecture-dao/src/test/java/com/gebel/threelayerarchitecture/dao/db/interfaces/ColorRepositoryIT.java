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

import com.gebel.threelayerarchitecture.dao.db.entity.ColorEntity;

import test.com.gebel.threelayerarchitecture.sandbox.container.MysqlDatabaseTestContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource("classpath:db/application-test-mysql.properties")
class ColorRepositoryIT {

	// Ex: c2bba799-02db-4b4b-8782-0df1517bbe1d
	private static final String UUID_REGEX = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
	
	// Shared between all methods.
	private static final MysqlDatabaseTestContainer MYSQL_CONTAINER = new MysqlDatabaseTestContainer("8.0.11", "cars_db", "test_user", "test_password");

	@Autowired
	private ColorRepository colorRepository;

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
	@Sql("classpath:db/color/findById_severalColors.sql")
	void givenSeveralColors_whenFindById_thenOneResult() {
		// Given + sql
		String id = "id_1";

		// When
		Optional<ColorEntity> optionalColor = colorRepository.findById(id);

		// Then
		ColorEntity foundColor = optionalColor.get();
		assertEquals("id_1", foundColor.getId());
		assertEquals("#000000", foundColor.getHexaCode());
	}

	@Test
	@Sql("classpath:db/color/findAll_severalColors.sql")
	void givenSeveralColors_whenFindAll_thenAllResults() {
		// Given sql

		// When
		List<ColorEntity> colors = colorRepository.findAll();

		// Then
		assertEquals(3, colors.size());

		ColorEntity color1 = colors.get(0);
		assertEquals("id_1", color1.getId());
		assertEquals("#000000", color1.getHexaCode());

		ColorEntity color2 = colors.get(1);
		assertEquals("id_2", color2.getId());
		assertEquals("#000001", color2.getHexaCode());

		ColorEntity color3 = colors.get(2);
		assertEquals("id_3", color3.getId());
		assertEquals("#000002", color3.getHexaCode());
	}
	
	@Test
	void givenNoColors_whenSave_thenColorCreated() {
		// Given + empty table
		ColorEntity colorToCreate = ColorEntity.builder()
			.id(null)
			.hexaCode("#123456")
			.build();

		// When
		ColorEntity createdColor = colorRepository.save(colorToCreate);

		// Then
		assertEquals(1, colorRepository.count());
		assertIdFormat(createdColor.getId());
		assertEquals("#123456", createdColor.getHexaCode());
	}
	
	private void assertIdFormat(String id) {
		Pattern pattern = Pattern.compile(UUID_REGEX);
		assertTrue(pattern.matcher(id).matches());
	}
	
	@Test
	@Sql("classpath:db/color/deleteById_severalColors.sql")
	void givenSeveralColors_whenDeleteById_thenColorDeleted() {
		// Given + sql
		String id = "id_1";
		assertTrue(colorRepository.findById(id).isPresent());
		assertEquals(3, colorRepository.count());

		// When
		colorRepository.deleteById(id);

		// Then
		assertFalse(colorRepository.findById(id).isPresent());
		assertEquals(2, colorRepository.count());
	}

	@Test
	void givenNoColors_whenFindOneByHexaCodeIgnoreCase_thenNoResult() {
		// Given + empty table
		String hexaCode = "#000000";

		// When
		Optional<ColorEntity> optionalColor = colorRepository.findOneByHexaCodeIgnoreCase(hexaCode);

		// Then
		assertTrue(optionalColor.isEmpty());
	}

	@Test
	@Sql("classpath:db/color/findOneByHexaCodeIgnoreCase_existingColor.sql")
	void givenExistingColor_whenFindOneByHexaCodeIgnoreCase_thenOneResult() {
		// Given + sql
		String hexaCode = "#000000";

		// When
		Optional<ColorEntity> optionalColor = colorRepository.findOneByHexaCodeIgnoreCase(hexaCode);

		// Then
		ColorEntity foundColor = optionalColor.get();
		assertEquals("id_1", foundColor.getId());
		assertEquals("#000000", foundColor.getHexaCode());
	}

}