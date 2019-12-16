package com.quart.job.test;

import org.quartz.JobExecutionContext;

import static org.junit.jupiter.api.Assertions.*;

class TaskJobLogTest {

    @org.junit.jupiter.api.Test
    void executeInternal() {
        System.out.println("hello world!");
    }
}