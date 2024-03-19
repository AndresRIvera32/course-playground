package com.springcloud.msvc.msvc.eventuate.application;

public interface CommandSender {

    void send(String topic, String eventType, String payload);
}
