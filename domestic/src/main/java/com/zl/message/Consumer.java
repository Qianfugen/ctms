package com.zl.message;

import com.rabbitmq.client.Channel;
import com.zl.config.RabbitMqConfig;
import com.zl.pojo.Transfer;
import com.zl.service.TransferService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class Consumer {
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private TransferService ts;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMqConfig.DOMESTIC_QUEUE_NAME)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void process(Message message, Channel channel, @Payload Map map) {
        System.out.println("---->收到消息：" + map);
        String dealNo = (String) map.get("dealNo");
        if (ts.queryTransferByDealNo(dealNo) == null) {
            System.out.println("未处理的消息。。。。。");
            //添加事务管理
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setName("SomeTxName");
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            //设置回滚点
            TransactionStatus status = transactionManager.getTransaction(def);

            try {
                Transfer transfer = new Transfer();
                transfer.setDealNo(dealNo);
                transfer.setTransFund(new BigDecimal(map.get("transFund").toString()));
                transfer.setCurrency(map.get("currency").toString());
                transfer.setTransType(Integer.valueOf(map.get("transType").toString()));
                transfer.setAccIn(map.get("accIn").toString());
                transfer.setAccInBank(map.get("accInBank").toString());
                transfer.setAccInName(map.get("accInName").toString());
                transfer.setAccOut(map.get("accOut").toString());
                transfer.setAccOutBank(map.get("accOutBank").toString());
                transfer.setAccOutName(map.get("accOutName").toString());
                transfer.setKind(map.get("kind").toString());
                //处理消息
                System.out.println("处理中。。。");
                if (ts.processMessage(transfer) > 0) {
                    System.out.println("插入成功。。。");
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                    System.out.println("---->消息处理成功，返回通知！！");
                    Map returnMessage = new HashMap();
                    map.put("dealNo", transfer.getDealNo());
                    map.put("transStatus", 1);
//                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    rabbitTemplate.convertAndSend("directExchange2", RabbitMqConfig.ROUTINGKEY_D, map);
                } else {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
                    System.out.println("处理失败。。。");
                }
            } catch (Exception e) {
                //事务回滚
                transactionManager.rollback(status);
                e.printStackTrace();
            }

        } else {
            System.out.println("--->消息已处理过，直接返回通知");
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                Map returnMessage = new HashMap();
                map.put("dealNo", dealNo);
                map.put("transStatus", 1);
//                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.convertAndSend("directExchange2", RabbitMqConfig.ROUTINGKEY_D, map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
