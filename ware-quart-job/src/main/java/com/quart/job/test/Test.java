package com.quart.job.test;

import com.quart.job.entity.ScheduleJobBean;
import com.quart.job.entity.ScheduleJobLogBean;
import com.quart.job.service.ScheduleJobLogService;
import com.quart.job.service.ScheduleJobService;
import com.quart.job.service.impl.ScheduleJobServiceImpl;
import com.quart.job.utils.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.spi.TriggerFiredBundle;

import java.lang.reflect.Method;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        Scheduler scheduler;
        TriggerFiredBundle firedBundle;
        Job job;
//        JobExecutionContext context=new JobExecutionContextImpl();

        ScheduleJobBean scheduleJobBean;
        ScheduleJobService scheduleJobService=new ScheduleJobServiceImpl();
    }
    public static void executeInternal(JobExecutionContext context){
        ScheduleJobBean jobBean = (ScheduleJobBean)context.getMergedJobDataMap().get(ScheduleJobBean.JOB_PARAM_KEY) ;
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) SpringContextUtil.getBean("scheduleJobLogService") ;
        // 定时器日志记录
        ScheduleJobLogBean logBean = new ScheduleJobLogBean () ;
        logBean.setJobId(jobBean.getJobId());
        logBean.setBeanName(jobBean.getBeanName());
        logBean.setParams(jobBean.getParams());
        logBean.setCreateTime(new Date());
        System.out.println(logBean);
//        long beginTime = System.currentTimeMillis() ;
//        try {
//            // 加载并执行定时器的 run 方法
//            Object target = SpringContextUtil.getBean(jobBean.getBeanName());
//            Method method = target.getClass().getDeclaredMethod("run", String.class);
//            method.invoke(target, jobBean.getParams());
//            long executeTime = System.currentTimeMillis() - beginTime;
//            logBean.setTimes((int)executeTime);
//            logBean.setStatus(0);
//            LOG.info("定时器 === >> "+jobBean.getJobId()+"执行成功,耗时 === >> " + executeTime);
//        } catch (Exception e){
//            // 异常信息
//            long executeTime = System.currentTimeMillis() - beginTime;
//            logBean.setTimes((int)executeTime);
//            logBean.setStatus(1);
//            logBean.setError(e.getMessage());
//        } finally {
//            scheduleJobLogService.insert(logBean) ;
//        }
    }
}
