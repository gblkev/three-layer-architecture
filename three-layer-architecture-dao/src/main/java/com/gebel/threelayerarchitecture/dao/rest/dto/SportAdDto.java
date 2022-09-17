package com.gebel.threelayerarchitecture.dao.rest.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SportAdDto {
	
	private String id;
	private LocalDateTime expirationDate;
	private String message;

}