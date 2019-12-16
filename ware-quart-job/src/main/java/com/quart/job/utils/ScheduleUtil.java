package com.quart.job.utils;

import com.quart.job.entity.ScheduleJobBean;
import org.quartz.*;

/**
 * 定时器工具类
 */
public class ScheduleUtil {

    private ScheduleUtil (){}

    private static final String SCHEDULE_NAME = "HUSKY_" ;

    /**
     * 触发器 KEY
     */
    public static TriggerKey getTriggerKey(Long jobId){
        return TriggerKey.triggerKey(SCHEDULE_NAME+jobId) ;
    }

    /**
     * 定时器 Key
     */
    public static JobKey getJobKey (Long jobId){
        return JobKey.jobKey(SCHEDULE_NAME+jobId) ;
    }

    /**
     * 表达式触发器
     */
    public static CronTrigger getCronTrigger (Scheduler scheduler,Long jobId){
        try {
            return (CronTrigger)scheduler.getTrigger(getTriggerKey(jobId)) ;
        } catch (SchedulerException e){
            throw new RuntimeException("getCronTrigger Fail",e) ;
        }
    }

    /**
     * 创建定时器
     */
    public static void createJob (Scheduler scheduler, ScheduleJobBean scheduleJob){
        try {
            // 构建定时器
            JobDetail jobDetail = JobBuilder.newJob(TaskJobLog.class).withIdentity(getJobKey(scheduleJob.getJobId())).build() ;
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                    .cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing() ;
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(getTriggerKey(scheduleJob.getJobId()))
                    .withSchedule(scheduleBuilder).build() ;
            jobDetail.getJobDataMap().put(ScheduleJobBean.JOB_PARAM_KEY,scheduleJob);
            scheduler.scheduleJob(jobDetail,trigger) ;
            // 如果该定时器处于暂停状态
            if (scheduleJob.getStatus() == 1){
                pauseJob(scheduler,scheduleJob.getJobId()) ;
            }
        } catch (SchedulerException e){
            throw new RuntimeException("createJob Fail",e) ;
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateJob(Scheduler scheduler, ScheduleJobBean scheduleJob) {
        try {
            // 构建定时器
            TriggerKey triggerKey = getTriggerKey(scheduleJob.getJobId());
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = getCronTrigger(scheduler, scheduleJob.getJobId());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            trigger.getJobDataMap().put(ScheduleJobBean.JOB_PARAM_KEY, scheduleJob);
            scheduler.rescheduleJob(triggerKey, trigger);
            // 如果该定时器处于暂停状态
            if(scheduleJob.getStatus() == 1){
                pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("updateJob Fail",e) ;
        }
    }

    /**
     * 停止定时器
     */
    public static void pauseJob (Scheduler scheduler,Long jobId){
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e){
            throw new RuntimeException("pauseJob Fail",e) ;
        }
    }

    /**
     * 恢复定时器
     */
    public static void resumeJob (Scheduler scheduler,Long jobId){
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e){
            throw new RuntimeException("resumeJob Fail",e) ;
        }
    }

    /**
     * 删除定时器
     */
    public static void deleteJob (Scheduler scheduler,Long jobId){
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e){
            throw new RuntimeException("deleteJob Fail",e) ;
        }
    }

    /**
     * 执行定时器
     */
    public static void run (Scheduler scheduler, ScheduleJobBean scheduleJob){
        try {
            JobDataMap dataMap = new JobDataMap() ;
            dataMap.put(ScheduleJobBean.JOB_PARAM_KEY,scheduleJob);
            scheduler.triggerJob(getJobKey(scheduleJob.getJobId()),dataMap);
        } catch (SchedulerException e){
            throw new RuntimeException("run Fail",e) ;
        }
    }
}
