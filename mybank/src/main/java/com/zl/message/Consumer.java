package com.zl.message;

import com.rabbitmq.client.Channel;
import com.zl.config.RabbitMqConfig;
import com.zl.service.TransferService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMqConfig.RETURN_OVER_QUEUE_NAME)
public class Consumer {
    @Autowired
    private TransferService ts;
    @RabbitHandler
    public void process(Message message, Channel channel, Map map){
        System.out.println("消息："+map);

        String dealNo= (String) map.get("dealNo");
        //查询该流水号记录是否已处理完成
        if(ts.queryTransferDealing(dealNo)!=null){
            //设置流水记录为完成
            if(ts.transferConfirm(dealNo)>0){
                System.out.println("修改记录状态已成功");
                try {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
