package com.socket.server.socket;


import com.socket.server.config.Config;
import com.socket.server.zookeeper.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
@Component
@Slf4j
public class TCPServer {
    //本地socket服务器地址
    private String host;
    //服务器运行状态
    private volatile boolean isRunning = false;
    //处理Accept连接事件的线程，这里线程数设置为1即可，netty处理链接事件默认为单线程，过度设置反而浪费cpu资源
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    //处理hadnler的工作线程，其实也就是处理IO读写 。线程数据默认为 CPU 核心数乘以2
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    @Value("${zookeeper.address}")
    private String zkAddress;

    //zookeeper 配置
    private ServiceRegistry serviceRegistry;

    @Autowired
    private Config config;

    @Autowired
    private ServerChannelInitializer serverChannelInitializer;

    public TCPServer() {}

    /**
     * 服务启动
     */
    public synchronized void startServer() {
        try {
            this.init();
        }catch(Exception ex) {
            System.out.println(ex);
        }
    }

    public void init() throws Exception{


        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(serverChannelInitializer);
        //获取本地IP 和 服务端口
        InetAddress localHost = InetAddress.getLocalHost();
        this.host = localHost.getHostAddress();
        int i = Integer.parseInt(config.getPort());
        ChannelFuture future = bootstrap.bind(host,i).sync();
        if (future.isSuccess()) {
            log.info("server start...");
        }
        if (serviceRegistry == null) {
            serviceRegistry = new ServiceRegistry(zkAddress);
        }
        //向zk 注册服务 地址
        serviceRegistry.register(host + ":" + config.getPort());

      /*  //创建ServerBootstrap实例
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        //初始化ServerBootstrap的线程组
        serverBootstrap.group(bossGroup,workerGroup);//
        //设置将要被实例化的ServerChannel类
        serverBootstrap.channel(NioServerSocketChannel.class);//
        //在ServerChannelInitializer中初始化ChannelPipeline责任链，并添加到serverBootstrap中
        serverBootstrap.childHandler(new ServerChannelInitializer());
        //标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        // 是否启用心跳保活机机制
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        //绑定端口后，开启监听
        ChannelFuture channelFuture = serverBootstrap.bind(config.getPort()).sync();
        if(channelFuture.isSuccess()){
            System.out.println("TCP服务启动 成功---------------");
        }*/
    }



    /**
     * 服务关闭
     */
    public synchronized void stopServer() {
        if (!this.isRunning) {
            throw new IllegalStateException(this.getName() + " 未启动 .");
        }
        this.isRunning = false;
        try {
            Future<?> future = this.workerGroup.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("workerGroup 无法正常停止:{}", future.cause());
            }

            future = this.bossGroup.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("bossGroup 无法正常停止:{}", future.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.log.info("TCP服务已经停止...");
    }

    private String getName() {
        return "TCP-Server";
    }

    public ServerChannelInitializer getServerChannelInitializer() {
        return serverChannelInitializer;
    }

    public void setServerChannelInitializer(ServerChannelInitializer serverChannelInitializer) {
        this.serverChannelInitializer = serverChannelInitializer;
    }
}
