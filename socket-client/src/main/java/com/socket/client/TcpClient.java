package com.socket.client;


import com.socket.client.tcpSocket.SocketClient;
import com.socket.server.model.Demo;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class TcpClient {


    public static void main(String[] args) throws IOException {
        new SocketClient().sendSocket(new Demo());

    }



}
