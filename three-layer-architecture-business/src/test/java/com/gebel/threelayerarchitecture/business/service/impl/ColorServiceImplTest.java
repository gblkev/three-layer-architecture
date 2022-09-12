package com.gebel.threelayerarchitecture.business.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gebel.threelayerarchitecture.business.domain.BusinessErrorCode;
import com.gebel.threelayerarchitecture.business.domain.BusinessException;
import com.gebel.threelayerarchitecture.business.domain.Color;
import com.gebel.threelayerarchitecture.business.service.converter.DomainColorConverter;
import com.gebel.threelayerarchitecture.dao.db.entity.ColorEntity;
import com.gebel.threelayerarchitecture.dao.db.interfaces.ColorRepository;

@ExtendWith(MockitoExtension.class)
class ColorServiceImplTest {
	
	@Mock
	private ColorRepository colorRepository;
	
	private ColorServiceImpl colorService;
	
	@BeforeEach
	void setup() {
		colorService = new ColorServiceImpl(colorRepository, new DomainColorConverter());
	}
	
	@Test
	void givenExistingColor_whenGetById_thenColorRetrieved() {
		// Given
		String colorId = "test_id1";
		ColorEntity entityColor = ColorEntity.builder()
			.id(colorId)
			.hexaCode("#000001")
			.build();
		when(colorRepository.findById(colorId))
			.thenReturn(Optional.of(entityColor));
		
		// When
		Color domainColor = colorService.getColorById(colorId);
		
		// Then
		assertEquals("test_id1", domainColor.getId());
		assertEquals("#000001", domainColor.getHexaCode());
	}
	
	@Test
	void givenNonExistingColor_whenGetById_thenNullReturned() {
		// Given
		String colorId = "test_id1";
		when(colorRepository.findById(colorId))
			.thenReturn(Optional.empty());
		
		// When
		Color domainColor = colorService.getColorById(colorId);
		
		// Then
		assertNull(domainColor);
	}

	@Test
	void givenSeveralColors_whenGetAllColors_thenAllColorsRetrieved() {
		// Given
		ColorEntity entityColor1 = ColorEntity.builder()
			.id("test_id1")
			.hexaCode("#000001")
			.build();
		ColorEntity entityColor2 = ColorEntity.builder()
			.id("test_id2")
			.hexaCode("#000002")
			.build();
		List<ColorEntity> entitiesColors = List.of(entityColor1, entityColor2);
		when(colorRepository.findAll())
			.thenReturn(entitiesColors);
		
		// When
		List<Color> domainColors = colorService.getAllColors();
		
		// Then
		assertEquals(2, domainColors.size());
		
		Color domainColor1 = domainColors.get(0);
		assertEquals("test_id1", domainColor1.getId());
		assertEquals("#000001", domainColor1.getHexaCode());
		
		Color domainColor2 = domainColors.get(1);
		assertEquals("test_id2", domainColor2.getId());
		assertEquals("#000002", domainColor2.getHexaCode());
	}
	
	@Test
	void givenSeveralColors_whenCountColors_thenRightCount() {
		// Given
		when(colorRepository.count())
			.thenReturn(17L);
		
		// When
		long count = colorService.countColors();
		
		// Then
		assertEquals(17, count);
	}
	
	@Test
	void givenValidColor_whenCreateColor_thenColorCreated() throws BusinessException {
		// Given
		ColorEntity createdEntityColor = ColorEntity.builder()
			.id("test_id")
			.hexaCode("#000000")
			.build();
		ArgumentCaptor<ColorEntity> colorEntityArgumentCaptor = ArgumentCaptor.forClass(ColorEntity.class);
		when(colorRepository.save(colorEntityArgumentCaptor.capture()))
			.thenReturn(createdEntityColor);
		
		String hexaCodeOfColorToCreate = "#000000";
		
		// When
		Color createdDomainColor = colorService.createColor(hexaCodeOfColorToCreate);
		
		// Then
		assertNull(colorEntityArgumentCaptor.getValue().getId());
		assertEquals("#000000", colorEntityArgumentCaptor.getValue().getHexaCode());
		
		assertEquals("test_id", createdDomainColor.getId());
		assertEquals("#000000", createdDomainColor.getHexaCode());
	}
	
	@Test
	void givenInvalidHexadecimalCode_whenCreateColor_thenThrowBusinessException() throws BusinessException {
		// Given
		String hexaCodeOfColorToCreate = null;
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			colorService.createColor(hexaCodeOfColorToCreate);
		});

		// Then
		verifyNoInteractions(colorRepository);
		assertEquals(BusinessErrorCode.COLOR_INVALID_HEXA_CODE, businessException.getBusinessError());
	}
	
	@Test
	void givenAlreadyExistingColor_whenCreateColor_thenThrowBusinessException() throws BusinessException {
		// Given
		String hexaCodeOfColorToCreate = "#000000";
		ColorEntity existingEntityColor = ColorEntity.builder()
			.id("test_id")
			.hexaCode("#000000")
			.build();
		when(colorRepository.findOneByHexaCodeIgnoreCase("#000000"))
			.thenReturn(Optional.of(existingEntityColor));
		
		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			// When
			colorService.createColor(hexaCodeOfColorToCreate);
		});

		// Then
		verify(colorRepository, never())
			.save(any());
		assertEquals(BusinessErrorCode.COLOR_SAME_HEXA_CODE_ALREADY_EXISTS, businessException.getBusinessError());
	}
	
	@Test
	void givenExistingColor_whenDeleteColor_thenColorDeleted() {
		// Given
		String existingColorId = "existing_id";
		when(colorRepository.existsById("existing_id"))
			.thenReturn(true);
		
		// When
		colorService.deleteColor(existingColorId);
		
		// Then
		verify(colorRepository, times(1))
			.deleteById("existing_id");
	}
	
	@Test
	void givenNonExistingColor_whenDeleteColor_thenDeleteNotDone() {
		// Given
		String nonExistingColorId = "non_existing_id";
		when(colorRepository.existsById("non_existing_id"))
			.thenReturn(false);
		
		// When
		colorService.deleteColor(nonExistingColorId);
		
		// Then
		verify(colorRepository,never())
			.deleteById(any());
	}
	
}