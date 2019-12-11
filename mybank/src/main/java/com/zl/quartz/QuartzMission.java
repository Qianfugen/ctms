package com.zl.quartz;

import com.zl.pojo.Coll;
import com.zl.service.CashSweepService;
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

    /**
     * 资金归集方法，从子账号将资金高于签约的部分转出到主账户
     * 定时任务,每月最后一日的上午10:15触发
     */
    @Scheduled(cron = "0 15 10 * * ?")
    public void sweepCash(){
        List<Coll> colls = cashSweepService.queryAllCollInTable();
        if(colls!=null){
            cashSweepService.sweepCash(colls);
        }
        System.out.println("归集完成");
    }
}
