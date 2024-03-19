package com.springcloud.msvc.courses.repositories;

import com.springcloud.msvc.courses.models.entity.OutboxEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxDao extends CrudRepository<OutboxEvent, Long> {

}
