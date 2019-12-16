package com.zl.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public final static String DOMESTIC_QUEUE_NAME="domestic";
    public final static String OVERSEAS_QUEUE_NAME="overseas";
    public final static String RETURN_OVER_QUEUE_NAME="returnOver";
    public final static String ROUTINGKEY_A="toDome";
    public final static String ROUTINGKEY_B="toOver";
    public final static String ROUTINGKEY_C="reO";


    @Bean(RabbitMqConfig.DOMESTIC_QUEUE_NAME)
    public Queue queueA() {
        return QueueBuilder.durable(DOMESTIC_QUEUE_NAME).build();
    }

    @Bean(RabbitMqConfig.OVERSEAS_QUEUE_NAME)
    public Queue queueB() {
        return QueueBuilder.durable(OVERSEAS_QUEUE_NAME).build();
    }

    @Bean("directExchange")
    Exchange exchange() {
        return ExchangeBuilder.directExchange("directExchange").durable(true).build();
    }

    @Bean
    Binding bindingExchangeMessage1(@Qualifier(RabbitMqConfig.DOMESTIC_QUEUE_NAME) Queue queue, @Qualifier("directExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConfig.ROUTINGKEY_A).noargs();
    }

    @Bean
    Binding bindingExchangeMessage2(@Qualifier(RabbitMqConfig.OVERSEAS_QUEUE_NAME) Queue queue, @Qualifier("directExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConfig.ROUTINGKEY_B).noargs();
    }

    @Bean(RabbitMqConfig.RETURN_OVER_QUEUE_NAME)
    public Queue queueC() {
        return QueueBuilder.durable(RETURN_OVER_QUEUE_NAME).build();
    }
    @Bean("directExchange2")
    Exchange exchange2() {
        return ExchangeBuilder.directExchange("directExchange2").durable(true).build();
    }

    @Bean
    Binding bindingExchangeMessage3(@Qualifier(RabbitMqConfig.RETURN_OVER_QUEUE_NAME) Queue queue, @Qualifier("directExchange2") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConfig.ROUTINGKEY_C).noargs();
    }

//    @Bean
//    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        return factory;
//    }
}
