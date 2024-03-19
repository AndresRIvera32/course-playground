package com.springcloud.msvc.msvc.eventuate.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "outbox")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_payload")
    private String eventPayload;

    @Column(name = "is_processed_event")
    private boolean isProcessed;

    @PersistenceCreator
    public OutboxEvent(String eventType, String eventPayload){
        this.eventType = eventType;
        this.eventPayload = eventPayload;
        this.isProcessed = false;
    }

    public void markEventAsProcessed(){
        this.isProcessed = true;
    }
}
