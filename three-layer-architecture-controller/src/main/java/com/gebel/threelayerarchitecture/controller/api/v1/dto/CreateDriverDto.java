package com.gebel.threelayerarchitecture.controller.api.v1.dto;

import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDriverDto {
	
	@Schema(required = true, example = "Forrest", pattern = "Between 1 and 30 characters, any character")
	private String firstName;
	
	@Schema(required = true, example = "Gump", pattern = "Between 1 and 30 characters, any character")
	private String lastName;

	@Override
	public String toString() {
		return "CreateDriverDto [cidCompliantFirstName=" + getCidCompliantName(firstName) + ", cidCompliantLastName=" + getCidCompliantName(lastName) + "]";
	}
	
	private String getCidCompliantName(String name) {
		return replaceAllCharactersWithWildcardExceptFirstLetter(name);
	}
	
	private String replaceAllCharactersWithWildcardExceptFirstLetter(String s) {
		if (StringUtils.isEmpty(s)) {
			return s;
		}
		return s.charAt(0) + "******";
	}
	
}