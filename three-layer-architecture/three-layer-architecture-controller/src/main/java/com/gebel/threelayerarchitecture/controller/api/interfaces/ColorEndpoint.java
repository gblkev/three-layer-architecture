package com.gebel.threelayerarchitecture.controller.api.interfaces;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gebel.threelayerarchitecture.controller.api.dto.ColorDto;

// TODO : add swagger
@RequestMapping("/colors")
public interface ColorEndpoint {

	@GetMapping("/")
	List<ColorDto> getAllAvailableColors();
	
}