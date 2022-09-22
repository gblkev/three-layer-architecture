package com.gebel.threelayerarchitecture.dao.rest.dto;

import java.time.LocalDateTime;

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
	private LocalDateTime expirationDate;
	private String message;

}