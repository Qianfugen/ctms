package com.zl.api;

import com.zl.pojo.Job;
import com.zl.pojo.ScheduleJobBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@FeignClient("ware-quart-job")
public interface JobAPI {

    /**
     * 添加定时器
     */
    @ResponseBody
    @RequestMapping("/quart-job/job/insertJob")
    public ScheduleJobBean insertJob(@RequestBody Job job);

    /**
     * 执行一次定时器
     */
    @RequestMapping("/quart-job/job/runJob")
    public String runJob ();

    /**
     * 更新定时器
     */
    @RequestMapping("/quart-job/job/updateJob")
    public String updateJob ();

    /**
     * 停止定时器
     */
    @RequestMapping("/quart-job/job/pauseJob")
    public String pauseJob ();

    /**
     * 恢复定时器
     */
    @RequestMapping("/quart-job/job/resumeJob")
    public String resumeJob ();

    /**
     * 删除定时器
     */
    @RequestMapping("/quart-job/job/deleteJob")
    public String deleteJob (@RequestParam("accNo") String accNo);

}
