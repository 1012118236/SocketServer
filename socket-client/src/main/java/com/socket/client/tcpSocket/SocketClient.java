package com.socket.client.tcpSocket;

import com.socket.client.utils.TCPUtil;
import com.socket.client.zookeeper.ServiceDiscovery;
import com.socket.server.model.config.ZookeeperConf;


public class SocketClient {

    private void SocketClient(){}

    public void sendSocket(Object message){
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(ZookeeperConf.discoveryAddress);
        String serverAddress = serviceDiscovery.discover();
        if (serverAddress!=null) {
            String[] array = serverAddress.split(":");
            if (array!=null && array.length==2) {
                String host = array[0];
                String port = array[1];
                //log.info(" netty server path is {}",serverAddress);
                TCPUtil.sendTCPRequest(host,port,"hello","UTF-8");

            }
        }
    }
}
