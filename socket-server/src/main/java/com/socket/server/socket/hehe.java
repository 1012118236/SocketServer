package com.socket.server.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hehe {

    @Autowired
    public KafkaTemplate kafkaTemplate;

    @RequestMapping(value = "/hehe")
    public String hehe(){
        kafkaTemplate.send("message","1232");

        return "hehe";
    }

}
