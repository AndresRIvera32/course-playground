package com.springcloud.msvc.msvc.eventuate.infraestructure.repositories;

import com.springcloud.msvc.msvc.eventuate.models.OutboxEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxDao extends CrudRepository<OutboxEvent, Long> {

    List<OutboxEvent> findByIsProcessedFalse();
}
