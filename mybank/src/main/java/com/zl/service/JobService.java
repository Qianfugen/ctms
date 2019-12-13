package com.zl.service;

import com.zl.pojo.Job;

public interface JobService {
    /**
     * 获取定时任务
     * @param
     * @return
     */
    public Job getJob(String accIn);

    /**
     * 设置定时任务
     * @param job 任务
     * @return
     */
    public int setJob(Job job);
}
