package com.zl.message;

import com.rabbitmq.client.Channel;
import com.zl.config.RabbitMqConfig;
import com.zl.pojo.Transfer;
import com.zl.service.TransferService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class Consumer {
    @Autowired
    private TransferService ts;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 处理境外银行返回的消息
     *
     * @param message
     * @param channel
     * @param map
     */
    @RabbitListener(queues = RabbitMqConfig.RETURN_OVER_QUEUE_NAME)
    public void process(Message message, Channel channel, @Payload Map map) {
        System.out.println("over:收到消息：" + map);

        String dealNo = (String) map.get("dealNo");
        //查询该流水号记录是否已处理完成
        List<Transfer> overDealing = (List<Transfer>) redisTemplate.opsForList().leftPop("overDealing");
        Integer removeIndex = null;
        //redis中没有记录则从数据库中查询数据
        if (overDealing == null) {
            overDealing = ts.queryAllOverDealing();
            redisTemplate.opsForList().leftPush("overDealing", overDealing);
        }
        if (overDealing != null) {
            for (int i = 0; i < overDealing.size(); i++) {
                if (dealNo.equals(overDealing.get(i).getDealNo())) {
                    removeIndex = i;
                    break;
                }
            }
        }

        if (removeIndex != null) {
            //设置流水记录为完成
            if (ts.transferConfirm(dealNo) > 0) {
                overDealing.remove(removeIndex);
                redisTemplate.opsForList().leftPush("overDealing", overDealing);
                System.out.println("over:修改记录状态已成功");
                try {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("==>over:该记录已处理，重复消息直接返回");
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理境内银行返回的消息
     *
     * @param message
     * @param channel
     * @param map
     */
    @RabbitListener(queues = RabbitMqConfig.RETURN_DEME_QUEUE_NAME)
    public void process2(Message message, Channel channel, @Payload Map map) {
        System.out.println("domestic:收到消息：" + map);

        String dealNo = (String) map.get("dealNo");
        Integer removeIndex = null;
        List<Transfer> domeDealing = (List<Transfer>) redisTemplate.opsForList().leftPop("domeDealing");

        if (domeDealing == null) {
            domeDealing = ts.queryAllDomeDealing();
            redisTemplate.opsForList().leftPush("domeDealing", domeDealing);
        }
        if (domeDealing != null) {
            for (int i = 0; i < domeDealing.size(); i++) {
                if (dealNo.equals(domeDealing.get(i).getDealNo())) {
                    removeIndex = i;
                    break;
                }
            }
        }

        //查询该流水号记录是否已处理完成
        if (removeIndex != null) {
            //设置流水记录为完成
            if (ts.transferConfirm(dealNo) > 0) {
                domeDealing.remove(removeIndex);
                redisTemplate.opsForList().leftPush("domeDealing", domeDealing);
                System.out.println("domestic:修改记录状态已成功");
                try {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("==>domestic:该记录已处理，重复消息直接返回");
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
