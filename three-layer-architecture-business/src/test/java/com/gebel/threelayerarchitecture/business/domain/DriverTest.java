package com.gebel.threelayerarchitecture.business.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DriverTest {
	
	private static final String VALID_FIRST_NAME = "Forrest";
	private static final String VALID_LAST_NAME = "Gump";

	@ParameterizedTest
	@MethodSource("givenValidName")
	void givenValidFirstName_whenCleanAndValidate_thenValidFirstName(String validFirstNameInInput, String expectedFirstNameInOutput) throws BusinessException {
		// Given
		Driver driver = Driver.builder()
			.id("test_id")
			.firstName(validFirstNameInInput)
			.lastName(VALID_LAST_NAME)
			.build();
		
		// When
		driver.cleanAndValidate();
		
		// Then + no exception
		assertEquals("test_id", driver.getId());
		assertEquals(expectedFirstNameInOutput, driver.getFirstName());
		assertEquals(VALID_LAST_NAME, driver.getLastName());
	}
	
	static Stream<Arguments> givenValidName() {
		return Stream.of(
			// First name in input + first name in output.
			Arguments.of("A", "A"),
			Arguments.of(" A", "A"),
			Arguments.of(" A ", "A"),
			Arguments.of("abcdefghijklmnopqrstuvwxyz1234", "abcdefghijklmnopqrstuvwxyz1234"),
			Arguments.of(" abcdefghijklmnopqrstuvwxyz1234", "abcdefghijklmnopqrstuvwxyz1234"),
			Arguments.of("Jean-Pierre", "Jean-Pierre"),
			Arguments.of("Joe Joe", "Joe Joe"),
			Arguments.of("àäâåæéèëêēïîöôøœüûŭÿçčñð", "àäâåæéèëêēïîöôøœüûŭÿçčñð")
		);
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "abcdefghijklmnopqrstuvwxyz12345", "Joe\nJoe", "      " })
	void givenInvalidFirstName_whenCleanAndValidate_thenError(String invalidFirstName) throws BusinessException {
		// Given
		Driver driver = Driver.builder()
			.id("test_id")
			.firstName(invalidFirstName)
			.lastName(VALID_LAST_NAME)
			.build();
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			driver.cleanAndValidate();
		});

		// Then
		assertEquals(BusinessErrorCode.DRIVER_INVALID_FIRST_NAME, businessException.getBusinessError());
	}
	
	@ParameterizedTest
	@MethodSource("givenValidName")
	void givenValidLastName_whenCleanAndValidate_thenValidLastName(String validLastNameInInput, String expectedLastNameInOutput) throws BusinessException {
		// Given
		Driver driver = Driver.builder()
			.id("test_id")
			.firstName(VALID_FIRST_NAME)
			.lastName(validLastNameInInput)
			.build();
		
		// When
		driver.cleanAndValidate();
		
		// Then + no exception
		assertEquals("test_id", driver.getId());
		assertEquals("Forrest", driver.getFirstName());
		assertEquals(expectedLastNameInOutput, driver.getLastName());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "abcdefghijklmnopqrstuvwxyz12345", "Joe\nJoe", "      " })
	void givenInvalidLastName_whenCleanAndValidate_thenError(String invalidLastName) throws BusinessException {
		// Given
		Driver driver = Driver.builder()
			.id("test_id")
			.firstName(VALID_FIRST_NAME)
			.lastName(invalidLastName)
			.build();
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			driver.cleanAndValidate();
		});

		// Then
		assertEquals(BusinessErrorCode.DRIVER_INVALID_LAST_NAME, businessException.getBusinessError());
	}
	
}