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
     */
    @Override
    public Job getJob(int id) {
        return jobDao.getJob(id);
    }

    /**
     * 设置定时任务
     *
     * @param job 任务
     * @return
     */
    @Override
    public int setJob(Job job) {
        System.out.println("job:"+job);
        System.out.println("这里是jobservice");
        return jobDao.setJob(job);
    }
}
