package com.gebel.threelayerarchitecture.controller.api;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {
	
	@Value("#{'${web.cors.allowed-origins}'.split(',')}")
	private List<String> corsAllowedOrigins;
	
	@Value("#{'${web.cors.allowed-methods}'.split(',')}")
	private List<String> corsAllowedMethods;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
					.allowedOrigins(trim(corsAllowedOrigins))
					.allowedMethods(trim(corsAllowedMethods));
			}
		};
	}
	
	private String[] trim(List<String> strings) {
		return CollectionUtils.emptyIfNull(strings)
			.stream()
			.map(String::trim)
			.toArray(String[]::new);
	}

}