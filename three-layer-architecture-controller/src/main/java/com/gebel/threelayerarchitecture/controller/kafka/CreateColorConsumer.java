package com.gebel.threelayerarchitecture.controller.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.gebel.threelayerarchitecture.business.service.interfaces.ColorService;
import com.gebel.threelayerarchitecture.controller.kafka.model.CreateColorModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CreateColorConsumer {
	
	public static final String KAFKA_TOPIC = "threelayerarchitecture.create-color";
	
	private final ColorService colorService;

	@KafkaListener(topics = KAFKA_TOPIC)
	private void handleCreateColorMessage(@Payload CreateColorModel createColorModel, Acknowledgment acknowledgment) {
		LOGGER.info("Creating color {}", createColorModel);
		try {
			if (createColorModel != null) {
				colorService.createColor(createColorModel.getHexaCode());
			}
			acknowledgment.acknowledge();
		}
		catch (Exception e) {
			LOGGER.error("Error while handling Kafka color creation message ({})", createColorModel, e);
		}
	}
	
}