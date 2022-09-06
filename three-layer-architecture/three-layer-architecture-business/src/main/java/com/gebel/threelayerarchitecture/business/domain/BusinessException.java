package com.gebel.threelayerarchitecture.business.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BusinessException extends Exception {

	private static final long serialVersionUID = 1348916528015303292L;
	
	@NonNull
	private BusinessError businessError;

}