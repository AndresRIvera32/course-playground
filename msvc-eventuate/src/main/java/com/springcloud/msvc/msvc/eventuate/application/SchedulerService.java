package com.springcloud.msvc.msvc.eventuate.application;

import com.springcloud.msvc.msvc.eventuate.infraestructure.repositories.OutboxDao;
import com.springcloud.msvc.msvc.eventuate.models.OutboxEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerService implements CommandSender{

    @Value("${course.kafka.topic.name}")
    String courseKafkaTopic;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    OutboxDao outboxDao;


    /**
     * Each 10 seconds goes to database reads the unprocessed records and publish them in message broker
     */
    @Scheduled(fixedRate = 10000)
    public void processOutboxEvents(){
        List<OutboxEvent> outboxEventList = outboxDao.findByIsProcessedFalse();
        outboxEventList.forEach(outboxEvent -> {
            send(courseKafkaTopic, outboxEvent.getEventType(), outboxEvent.getEventPayload());
            outboxEvent.markEventAsProcessed();
        });
        outboxDao.saveAll(outboxEventList);
    }

    @Override
    public void send(String topic, String eventType, String payload) {
        kafkaTemplate.send(topic, eventType, payload);
    }
}
