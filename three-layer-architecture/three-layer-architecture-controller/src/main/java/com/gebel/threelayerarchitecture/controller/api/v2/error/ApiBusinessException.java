package com.gebel.threelayerarchitecture.controller.api.v2.error;

import com.gebel.threelayerarchitecture.business.domain.BusinessException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ApiBusinessException extends RuntimeException {

	private static final long serialVersionUID = -5719111089723479924L;
	
	@NonNull
	private BusinessException businessException;

}