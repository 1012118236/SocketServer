package com.socket.server.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 开机启动tcp服务器
 */
@Component
@Slf4j
public class EnableTcpServer implements CommandLineRunner {

    @Autowired
    TCPServer tcpServer;
    @Value("${tcpServer.enable}")
    Boolean enableType;

    @Override
    public void run(String... args) throws Exception {
        if(enableType){
            log.info(" tcpServer.enable : {} TCP服务启动中.....",this.enableType);
            tcpServer.startServer();
        }
    }

    public TCPServer getTcpServer() {
        return tcpServer;
    }

    public void setTcpServer(TCPServer tcpServer) {
        this.tcpServer = tcpServer;
    }

}
