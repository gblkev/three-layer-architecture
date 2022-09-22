package com.gebel.threelayerarchitecture.business.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gebel.threelayerarchitecture.business.domain.DataReport;
import com.gebel.threelayerarchitecture.business.service.interfaces.BrandService;
import com.gebel.threelayerarchitecture.business.service.interfaces.CarService;
import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.business.service.interfaces.DriverService;

@ExtendWith(MockitoExtension.class)
class DataReportServiceImplTest {
	
	@Mock
	private ColorService colorService;
	
	@Mock
	private DriverService driverService;
	
	@Mock
	private CarService carService;
	
	@Mock
	private BrandService brandService;
	
	@InjectMocks
	private DataReportServiceImpl dataReportService;
	
	@Test
	void givenSeveralExistingColorsDriversAndCars_whenGenerateDataReport_thenValidDataReport() {
		// Given
		when(colorService.countColors())
			.thenReturn(4L);
		when(driverService.countDrivers())
			.thenReturn(18L);
		when(carService.countCars())
			.thenReturn(17L);
		when(brandService.countBrands())
			.thenReturn(6L);
		
		// When
		DataReport dataReport = dataReportService.generateDataReport();
		
		// Then
		assertEquals(4, dataReport.getColorsCount());
		assertEquals(18, dataReport.getDriversCount());
		assertEquals(17, dataReport.getCarsCount());
		assertEquals(6, dataReport.getBrandsCount());
	}

}