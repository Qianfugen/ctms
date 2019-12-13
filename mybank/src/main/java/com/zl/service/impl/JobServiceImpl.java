package com.zl.service.impl;

import com.zl.dao.JobDao;
import com.zl.pojo.Job;
import com.zl.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobDao jobDao;

    /**
     * 获取定时任务
     *
     * @param accIn@return
     */
    @Override
    public Job getJob(String accIn) {
        return jobDao.getJob(accIn);
    }

    /**
     * 设置定时任务
     *
     * @param job 任务
     * @return
     */
    @Override
    public int setJob(Job job) {
        return jobDao.setJob(job);
    }
}
