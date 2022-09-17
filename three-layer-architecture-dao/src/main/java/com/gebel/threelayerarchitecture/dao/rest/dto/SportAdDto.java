package com.gebel.threelayerarchitecture.dao.rest.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SportAdDto {
	
	private String id;
	private ZonedDateTime expirationDate;
	private String message;

}