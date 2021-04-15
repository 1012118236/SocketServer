package com.socket.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    public static KafkaTemplate kafkaTemplate;

    public static void send(String message, Object o) {
        kafkaTemplate.send(message,o);
    }
}
