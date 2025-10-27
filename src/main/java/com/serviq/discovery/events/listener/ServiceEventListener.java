package com.serviq.discovery.events.listener;

import com.serviq.provider.service.events.ServiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
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
    public void consumeServiceEvent(@Payload ServiceEvent serviceEvent,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                    @Header(KafkaHeaders.OFFSET) long offset,
                                    Acknowledgment acknowledgment) {
        try {
            log.info("Received ServiceEvent from topic: {}, partition: {}, offset: {}",
                    topic, partition, offset);
            log.info("ServiceEvent details: {}", serviceEvent);

            // Process the service event
            processServiceEvent(serviceEvent);

            // Manually commit the offset after successful processing
            acknowledgment.acknowledge();

            log.info("Successfully processed ServiceEvent with offset: {}", offset);

        } catch (Exception e) {
            log.error("Error processing ServiceEvent from offset: {}", offset, e);
            // Don't acknowledge - message will be reprocessed
            // Or implement your retry/DLQ logic here
        }
    }

    private void processServiceEvent(ServiceEvent serviceEvent) {
        // Add your business logic here
        log.info("Processing ServiceEvent: {}", serviceEvent);

        // Example: Access Avro fields
        // String eventId = serviceEvent.getEventId();
        // String eventType = serviceEvent.getEventType();
        // Long timestamp = serviceEvent.getTimestamp();

        // Your processing logic...
    }
}
