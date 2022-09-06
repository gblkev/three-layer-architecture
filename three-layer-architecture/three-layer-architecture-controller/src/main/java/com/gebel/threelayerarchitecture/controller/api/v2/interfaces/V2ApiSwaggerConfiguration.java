package com.gebel.threelayerarchitecture.controller.api.v2.interfaces;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gebel.threelayerarchitecture.controller.api.v1.interfaces.V1ApiBaseUri;

@Configuration
class V2ApiSwaggerConfiguration {

	@Bean
	GroupedOpenApi v2Apis() {
		return GroupedOpenApi.builder()
			.group("v2")
			.pathsToMatch(V2ApiBaseUri.API_V2_BASE_URI + "/**")
			.pathsToExclude(V1ApiBaseUri.API_V1_BASE_URI + "/**")
			.build();
	}

}