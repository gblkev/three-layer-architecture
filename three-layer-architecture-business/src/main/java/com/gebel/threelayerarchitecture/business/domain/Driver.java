package com.gebel.threelayerarchitecture.business.domain;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Driver {
	
	private static final String NAME_REGEX = "^(.{1,30})$"; // Any character except new line
	
	private String id;
	private String firstName;
	private String lastName;
	
	public void cleanAndValidate() throws BusinessException {
		clean();
		validate();
	}
	
	public void clean() {
		cleanFirstName();
		cleanLastName();
	}
	
	public void cleanFirstName() {
		firstName = StringUtils.trim(firstName);
	}
	
	public void cleanLastName() {
		lastName = StringUtils.trim(lastName);
	}
	
	public void validate() throws BusinessException {
		if (!isValidFirstName()) {
			throw new BusinessException("'" + firstName + "' is not a valid first name", BusinessErrorCode.DRIVER_INVALID_FIRST_NAME);
		}
		if (!isValidLastName()) {
			throw new BusinessException("'" + lastName + "' is not a valid last name", BusinessErrorCode.DRIVER_INVALID_LAST_NAME);
		}
	}

	public boolean isValidFirstName() {
		if (StringUtils.isBlank(firstName)) {
			return false;
		}
		return Pattern.compile(NAME_REGEX)
				.matcher(firstName)
				.matches();
	}
	
	public boolean isValidLastName() {
		if (StringUtils.isBlank(lastName)) {
			return false;
		}
		return Pattern.compile(NAME_REGEX)
				.matcher(lastName)
				.matches();
	}

}