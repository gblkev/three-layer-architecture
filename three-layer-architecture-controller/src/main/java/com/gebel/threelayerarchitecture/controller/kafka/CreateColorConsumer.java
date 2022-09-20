package com.gebel.threelayerarchitecture.controller.kafka;

import org.springframework.kafka.annotation.KafkaListener;

import com.gebel.threelayerarchitecture.controller.kafka.model.CreateColorModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateColorConsumer {

	@KafkaListener(topics = "threelayerarchitecture.create-color", groupId = "threelayerarchitecture")
	private void handleCreateColorMessage(CreateColorModel createColorModel) {
		LOGGER.info("Creating color {}", createColorModel);
	}
	
}