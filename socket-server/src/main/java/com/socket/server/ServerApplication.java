package com.socket.server;

import com.socket.server.config.Config;
import com.socket.server.socket.TCPServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServerApplication {



    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }


}
