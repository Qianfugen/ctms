package com.zl.quartz;

import com.zl.pojo.Coll;
import com.zl.service.CashSweepService;
import com.zl.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fm
 * @version 1.0
 * @date 2019/12/11 9:54
 */
@Component
public class QuartzMission {
    @Autowired
    private CashSweepService cashSweepService;
    @Autowired
    private TransferService transferService;

    /**
     * 资金归集方法，从子账号将资金高于签约的部分转出到主账户
     * 定时任务,每月一日早上九点触发
     */
    @Scheduled(cron = "0 0 9 1 * ? ")
    public void sweepCash(){
        List<Coll> colls = cashSweepService.queryAllCollInTable();
        if(colls!=null&&colls.size()!=0){
            cashSweepService.sweepCash(colls);
        }
    }

    /**
     * 每天中午12点重新发送消息队列
     */
    @Scheduled(cron = "0 0 3,6,9,12 * * ?")
    public void autoSend(){
        transferService.autoSend();
    }
}
