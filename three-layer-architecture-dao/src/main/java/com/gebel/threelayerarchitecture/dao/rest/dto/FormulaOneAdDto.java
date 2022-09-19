package com.gebel.threelayerarchitecture.dao.rest.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormulaOneAdDto {
	
	private String id;
	private ZonedDateTime expirationDate;
	private String message;

}