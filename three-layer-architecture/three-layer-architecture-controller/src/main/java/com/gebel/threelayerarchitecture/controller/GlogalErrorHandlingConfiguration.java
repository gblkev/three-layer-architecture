package com.gebel.threelayerarchitecture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.gebel.threelayerarchitecture.controller.api.v1.error.V1ApiExceptionHandler;

/**
 * This configuration redirects all spring webmvc exceptions (like 404 not found for instance) to the Api error handler (cf {@link V1ApiExceptionHandler}).
 */
@Configuration
@EnableWebMvc
public class GlogalErrorHandlingConfiguration {

	@Autowired
	private DispatcherServlet servlet;

	@Bean
	public CommandLineRunner getCommandLineRunner(ApplicationContext context) {
		servlet.setThrowExceptionIfNoHandlerFound(true);
		return args -> {};
	}

}