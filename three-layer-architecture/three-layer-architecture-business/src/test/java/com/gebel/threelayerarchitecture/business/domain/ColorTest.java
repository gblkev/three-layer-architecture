package com.gebel.threelayerarchitecture.business.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ColorTest {

	@ParameterizedTest
	@ValueSource(strings = { "#aaa", "#AAA", "#AAa", "#000000", "#ABCDEF", "#AA1199" })
	void givenValidHexadecimalCode_whenIsValidHexaCode_thenReturnTrue(String validHexadecimalCode) {
		// Given
		Color color = Color.builder()
			.id("test_id")
			.hexaCode(validHexadecimalCode)
			.build();
		
		// When
		boolean isValid = color.isValidHexaCode();
		
		// Then
		assertTrue(isValid);
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "#G00000", "#AAA AA", "000000", "0000000", "00000é" })
	void givenInvalidHexadecimalCode_whenIsValidHexaCode_thenReturnFalse(String validHexadecimalCode) {
		// Given
		Color color = Color.builder()
			.id("test_id")
			.hexaCode(validHexadecimalCode)
			.build();
		
		// When
		boolean isValid = color.isValidHexaCode();
		
		// Then
		assertFalse(isValid);
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "#aaa", "#AAA", "#AAa", "#000000", "#ABCDEF", "#AA1199" })
	void givenValidHexadecimalCode_whenValidate_thenNoException(String validHexadecimalCode) throws BusinessException {
		// Given
		Color color = Color.builder()
			.id("test_id")
			.hexaCode(validHexadecimalCode)
			.build();
		
		// When
		color.validate();
		
		// Then no exception
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "#G00000", "#AAA AA", "000000", "0000000", "00000é" })
	void givenInvalidHexadecimalCode_whenValidate_thenThrowBusinessException(String validHexadecimalCode) {
		// Given
		Color color = Color.builder()
			.id("test_id")
			.hexaCode(validHexadecimalCode)
			.build();
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			color.validate();
		});

		// Then
		assertEquals(BusinessErrorCode.COLOR_INVALID_HEXA_CODE, businessException.getBusinessError());
	}
	
}