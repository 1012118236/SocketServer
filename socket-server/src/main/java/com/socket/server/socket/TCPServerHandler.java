package com.socket.server.socket;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@ChannelHandler.Sharable
public class TCPServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    public KafkaTemplate kafkaTemplate;
    public TCPServerHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object source) throws Exception {
       String str = new String((byte[]) source,"UTF-8");
//        ByteArrayInputStream baos = new ByteArrayInputStream((byte[]) source);
//        ObjectInputStream in = new ObjectInputStream(baos);
//        Object o = in.readObject();

        kafkaTemplate.send("message", str);


        log.info("收到消息↓：");
        log.info(str.toString());
        ctx.writeAndFlush(source);// 收到及发送，这里如果没有writeAndFlush，上面声明的ByteBuf需要ReferenceCountUtil.release主动释放
        ctx.close();

    }

    @KafkaListener(topics = "message")
    @Transactional
    public void meterTaskList(ConsumerRecord<?, ?> record) {
        Object value = record.value();
        System.out.println("kafka message is :" +value.toString());
    }

    public  KafkaTemplate getKafkaTemplate() {
        return kafkaTemplate;
    }

    public  void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
