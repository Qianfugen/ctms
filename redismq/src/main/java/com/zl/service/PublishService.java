package com.zl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 七脉 描述：发布service
 */
@Service
public class PublishService {
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * @author 七脉 描述：发布方法
     * @param channel 消息发布订阅 主题
     * @param message 消息信息
     */
    public void publish(String channel, Object message) {
        // 该方法封装的 connection.publish(rawChannel, rawMessage);
        redisTemplate.convertAndSend(channel, message);
    }
}