package com.gebel.threelayerarchitecture.controller.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;

import com.gebel.threelayerarchitecture.controller.kafka.CreateColorConsumer;
import com.gebel.threelayerarchitecture.controller.kafka.model.CreateColorModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ManagedResource(
	objectName = "threelayerarchitecture:name=kafka.consumers",
	description = "The application consumes a Kafka queue. This JMX resource enables to add messages into this queue.")
public class KafkaConsumersJmxResource {

	private static final String CREATE_COLOR_TOPIC = CreateColorConsumer.KAFKA_TOPIC;
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@ManagedOperation(description = "Publish a message to create a color in the topic " + CREATE_COLOR_TOPIC)
	@ManagedOperationParameter(name = "hexaCode", description = "The hexadecimal value of the color (ex: #FFFFFF)")
	public void publishCreateColorMessageToKafka(String hexaCode) {
		try {
			unsafePublishCreateColorMessageToKafka(hexaCode);
		}
		catch (Exception e) {
			LOGGER.error("Error while publishing create color message to Kafka through JMX", e);
		}
	}
	
	public void unsafePublishCreateColorMessageToKafka(String hexaCode) {
		CreateColorModel createColorModel = new CreateColorModel(hexaCode);
		kafkaTemplate.send(CREATE_COLOR_TOPIC, createColorModel);
	}
	
}