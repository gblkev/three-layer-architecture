package com.gebel.threelayerarchitecture.controller.api.v1.interfaces;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gebel.threelayerarchitecture.controller.api.v2.interfaces.V2ApiBaseUri;

@Configuration
class V1ApiSwaggerConfiguration {

	@Bean
	GroupedOpenApi v1Apis() {
		return GroupedOpenApi.builder()
			.group("v1")
			.pathsToMatch(V1ApiBaseUri.API_V1_BASE_URI + "/**")
			.pathsToExclude(V2ApiBaseUri.API_V2_BASE_URI + "/**")
			.build();
	}
	
}