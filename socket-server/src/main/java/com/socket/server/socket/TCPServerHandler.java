package com.socket.server.socket;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
@Component
public class TCPServerHandler  extends ChannelInboundHandlerAdapter  {

    private static final Logger log = LoggerFactory.getLogger(TCPServerHandler.class);

    public TCPServerHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object source) throws Exception {
//        String str = new String((byte[]) source,"UTF-8");
        ByteArrayInputStream baos =new ByteArrayInputStream((byte[]) source);
        ObjectInputStream in = new ObjectInputStream(baos);
        Object o = in.readObject();


        log.info("收到消息↓：");
        log.info(o.toString());
        ctx.writeAndFlush(source);// 收到及发送，这里如果没有writeAndFlush，上面声明的ByteBuf需要ReferenceCountUtil.release主动释放
        ctx.close();

    }


}
