package com.gebel.threelayerarchitecture.controller._test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import lombok.Getter;

public class Log4jInMemoryAppender extends AbstractAppender {

	@Getter
	private final List<LogEvent> events = Collections.synchronizedList(new ArrayList<>());

	public Log4jInMemoryAppender() {
        super(Log4jInMemoryAppender.class.getSimpleName(), null, null, true, null);
    }

	@Override
	public void append(LogEvent event) {
		events.add(event);
	}
	
	public void setupAppender(Class<?> clazz) {
		start();
		org.apache.logging.log4j.Logger logger = LogManager.getLogger(clazz);
		org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger) logger;
		coreLogger.get().addAppender(this, Level.INFO, null);
	}
	
	public void removeAppender(Class<?> clazz) {
		org.apache.logging.log4j.Logger logger = LogManager.getLogger(clazz);
		org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger) logger;
		coreLogger.get().removeAppender(getName());
	}
	
	public List<String> getEventsMessagesAsString() {
		return events.stream()
			.map(event -> event.getMessage().getFormattedMessage())
			.collect(Collectors.toList());
	}

}