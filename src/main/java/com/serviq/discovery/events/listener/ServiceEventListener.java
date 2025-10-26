package com.serviq.discovery.events.listener;

import com.serviq.discovery.service.events.ServiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceEventListener {

    @KafkaListener(
            id = "service-event-listener",
            topics = "${kafka.topic.service-events}",
            groupId = "${kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeServiceEvent(@Payload ServiceEvent serviceEvent, Acknowledgment ack) {
        log.info("Consumed service event: {}", serviceEvent);
        ack.acknowledge();
    }
}
