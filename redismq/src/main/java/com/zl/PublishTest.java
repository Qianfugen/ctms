package com.zl;


import com.zl.redismq.RedismqApplication;
import com.zl.service.PublishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author 七脉
 * 描述：消息发布
 */
@SpringBootTest(classes = RedismqApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PublishTest {

//    @Autowired
//    private PublishService publishService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test() {
        for(int i=1; i<=10; i++){
            //向dj主题里发布10个消息
            redisTemplate.convertAndSend("dj", "like "+i+" 次");
//            publishService.publish("dj", "like "+i+" 次");
        }
    }
}