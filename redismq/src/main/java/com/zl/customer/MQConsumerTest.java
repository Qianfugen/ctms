package com.zl.customer;


import javax.annotation.PostConstruct;

import com.zl.redismq.RedismqApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

/**
 * @author 七脉
 * 描述：消费者测试类
 */
@SpringBootTest(classes = RedismqApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MQConsumerTest {
    @Autowired
    private StringRedisTemplate redisTemplate;
    //redis的消息队列直接使用redis数组实现
    private ListOperations<String, String> listRedis;

    /**
     * <br>描 述: 初始化时赋值
     * <br>作 者: shizhenwei
     * <br>历 史: (版本) 作者 时间 注释
     */
    @PostConstruct
    private void init(){
        listRedis = redisTemplate.opsForList();
    }

    @Test
    public void test() {
        while(true){
            //从右边取堆栈顺序取1~10个消息
            String msg = listRedis.rightPop("storage");
            if(StringUtils.isEmpty(msg)){
                System.out.println("消息已经全部取出了。。。。");
                break;
            }
            System.out.println("msg:"+msg);
        }
    }
}