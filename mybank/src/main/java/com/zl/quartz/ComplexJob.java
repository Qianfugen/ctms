package com.zl.quartz;

import com.zl.pojo.Job;
import com.zl.pojo.Transfer;
import com.zl.service.JobService;
import com.zl.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;


@Configuration
public class ComplexJob implements SchedulingConfigurer {
    @Autowired
    private JobService jobService;
    @Autowired
    private TransferService transferService;

    private Job job = null;
    private Transfer transfer = new Transfer();

    /**
     * 任务，触发器
     *
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                //转账
                if (job != null) {
                    transfer.setAccIn(job.getAccIn());
                    transfer.setAccOut(job.getAccOut());
                    transfer.setTransFund(job.getTransFund());
                    transfer.setCurrency(job.getCurrency());
                    transferService.transferMoney(transfer);
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                System.out.println("定时任务触发。。。");
                //获取定时任务
                job = jobService.getJob(1);
                if (job != null) {
                    String cron = job.getCron();
                    System.out.println("cron表达式："+cron);
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
                return null;
            }
        });
    }

}
