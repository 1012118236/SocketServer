package com.socket.server.socket;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hehe {

    @RequestMapping(value = "/hehe")
    public String hehe(){
        return "hehe";
    }

}
