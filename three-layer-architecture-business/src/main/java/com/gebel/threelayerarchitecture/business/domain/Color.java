package com.gebel.threelayerarchitecture.business.domain;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Color {
	
	private static final String HEXADECIMAL_COLOR_REGEX = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
	
	private String id;
	private String hexaCode;
	
	public void validate() throws BusinessException {
		if (!isValidHexaCode()) {
			throw new BusinessException("'" + hexaCode + "' is not a valid hexadecimal color value", BusinessErrorCode.COLOR_INVALID_HEXA_CODE);
		}
	}

	public boolean isValidHexaCode() {
		if (StringUtils.isBlank(hexaCode)) {
			return false;
		}
		return Pattern.compile(HEXADECIMAL_COLOR_REGEX)
				.matcher(hexaCode)
				.matches();
	}

}