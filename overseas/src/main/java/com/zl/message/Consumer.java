package com.zl.message;

import com.zl.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = RabbitMqConfig.OVERSEAS_QUEUE_NAME)
public class Consumer {
    @RabbitHandler
    public void process(Object map){
        System.out.println("收到消息："+map);
    }
}
