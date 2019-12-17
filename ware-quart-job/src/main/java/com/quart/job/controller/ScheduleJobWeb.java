package com.quart.job.controller;

import com.quart.job.entity.Job;
import com.quart.job.entity.ScheduleJobBean;
import com.quart.job.service.ScheduleJobService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/job")
public class ScheduleJobWeb {

    @Resource
    private ScheduleJobService scheduleJobService;

    /**
     * 添加定时器
     */
    @ResponseBody
    @RequestMapping("/insertJob")
    public ScheduleJobBean insertJob(@RequestBody Job job) {
        ScheduleJobBean scheduleJobBean = new ScheduleJobBean();
        scheduleJobBean.setJobId(job.getId());
        scheduleJobBean.setBeanName("transferTask");
        scheduleJobBean.setCronExpression(job.getCron());
        String params=job.getAccIn()+","+job.getAccOut()+","+job.getTransFund().toString()+","+job.getCurrency();
        System.out.println("得到的param："+params);
        scheduleJobBean.setParams(params);
        scheduleJobBean.setStatus(0);
        scheduleJobBean.setRemark("转账");
        scheduleJobBean.setCreateTime(new Date());
        scheduleJobService.insert(scheduleJobBean);
        return scheduleJobBean;
    }

    /**
     * 执行一次定时器
     */
    @RequestMapping("/runJob")
    public String runJob() {
        Long jobId = 1L;
        scheduleJobService.run(jobId);
        return "success";
    }

    /**
     * 更新定时器
     */
    @RequestMapping("/updateJob")
    public String updateJob() {
        Long jobId = 1L;
        ScheduleJobBean scheduleJobBean = scheduleJobService.selectByPrimaryKey(jobId);
        scheduleJobBean.setParams("Hello,Job_Quart");
        scheduleJobService.updateByPrimaryKeySelective(scheduleJobBean);
        return "success";
    }

    /**
     * 停止定时器
     */
    @RequestMapping("/pauseJob")
    public String pauseJob() {
        Long jobId = 1L;
        scheduleJobService.pauseJob(jobId);
        return "success";
    }

    /**
     * 恢复定时器
     */
    @RequestMapping("/resumeJob")
    public String resumeJob() {
        Long jobId = 1L;
        scheduleJobService.resumeJob(jobId);
        return "success";
    }

    /**
     * 删除定时器
     */
    @RequestMapping("/deleteJob")
    public String deleteJob() {
        Long jobId = 1L;
        scheduleJobService.delete(jobId);
        return "success";
    }

}
