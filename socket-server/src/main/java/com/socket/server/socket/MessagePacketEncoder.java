package com.socket.server.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

@Component
public class MessagePacketEncoder  extends MessageToByteEncoder<Object> {
    public MessagePacketEncoder()
    {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception
    {
        try {
            //在这之前可以实现编码工作。
            out.writeBytes((byte[])msg);
        }finally {

        }
    }
}
