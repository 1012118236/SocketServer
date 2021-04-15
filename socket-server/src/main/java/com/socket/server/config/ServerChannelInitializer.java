package com.socket.server.config;


import com.socket.server.socket.MessagePacketDecoder;
import com.socket.server.socket.MessagePacketEncoder;
import com.socket.server.socket.TCPServerHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(2);

    @Autowired
    private TCPServerHandler tcpServerHandler;

    public ServerChannelInitializer() throws InterruptedException {
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //IdleStateHandler心跳机制,如果超时触发Handle中userEventTrigger()方法
        pipeline.addLast("idleStateHandler",
                new IdleStateHandler(15, 0, 0, TimeUnit.MINUTES));
        // netty基于分割符的自带解码器，根据提供的分隔符解析报文，这里是0x7e;1024表示单条消息的最大长度，解码器在查找分隔符的时候，达到该长度还没找到的话会抛异常
//        pipeline.addLast(
//                new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[] { 0x7e }),
//                        Unpooled.copiedBuffer(new byte[] { 0x7e })));
        //自定义编解码器
        pipeline.addLast(
                new MessagePacketDecoder(),
                new MessagePacketEncoder()
        );
        //自定义Hadler
        pipeline.addLast("handler",tcpServerHandler);
        //自定义Hander,可用于处理耗时操作，不阻塞IO处理线程
//        pipeline.addLast(group,"BussinessHandler",new BussinessHandler());
    }

    public TCPServerHandler getTcpServerHandler() {
        return tcpServerHandler;
    }

    public void setTcpServerHandler(TCPServerHandler tcpServerHandler) {
        this.tcpServerHandler = tcpServerHandler;
    }
}
