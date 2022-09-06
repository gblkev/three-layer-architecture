package com.gebel.threelayerarchitecture.business.domain;

import java.util.regex.Pattern;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Color {
	
	private static final String HEXADECIMAL_COLOR_REGEX = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
	
	private String id;
	private String hexaCode;
	
	public void validate() throws BusinessException {
		if (!isValidHexaCode(hexaCode)) {
			throw new BusinessException(BusinessError.COLOR_INVALID_HEXA_CODE);
		}
	}

	public boolean isValidHexaCode(String hexaCode) {
		return Pattern.compile(HEXADECIMAL_COLOR_REGEX)
				.matcher(hexaCode)
				.matches();
	}

}