package com.zl.quartz;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;


/**
 * 任务调度
 *
 * @author root
 */
//@Configuration
public class MyJob {
    private void before() {
        System.out.println("任务开始执行");
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void run() {
        before();
        System.out.println("开始：" + System.currentTimeMillis());
        // TODO 业务
        System.out.println("任务执行中。。。");
        System.out.println("结束：" + System.currentTimeMillis());
        after();
    }

    private void after() {
        System.out.println("任务开始执行");
    }

}
