package com.gebel.threelayerarchitecture.business.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Driver {
	
	private String id;
	private String firstName;
	private String lastName;

}