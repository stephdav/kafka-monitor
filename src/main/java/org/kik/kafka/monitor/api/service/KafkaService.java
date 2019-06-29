package org.kik.kafka.monitor.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

	@Autowired
	private BroadcastService broadcast;

	public KafkaService() {
		// Empty constructor
	}

	@KafkaListener(topics = "#{'${kafka.consumer.topics}'.split(',')}", containerFactory = "kafkaListenerContainerFactory")
	public void listenGroupFoo(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
			@Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
		broadcast.broadcastMessage(topic, message, ts);
	}

}