package com.quart.job.task;

import com.quart.job.api.MybankAPI;
import com.quart.job.entity.Transfer;
import com.quart.job.service.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("transferTask")
public class TransferTask implements TransferService {
    @Autowired
    private MybankAPI mybankAPI;
    @Autowired
    private ScheduleJobService scheduleJobService;

    @Override
    public void run(String params) {
        String[] bankInfo = params.split(",");
        String accIn = bankInfo[0];
        String accOut = bankInfo[1];
        String transferFund = bankInfo[2];
        String currency = bankInfo[3];
        System.out.println("accIn:" + accIn);
        System.out.println("accOut:" + accOut);
        System.out.println("transferFund:" + transferFund);
        System.out.println("currency:" + currency);
        Transfer transfer = new Transfer();
        transfer.setAccIn(accIn);
        transfer.setAccOut(accOut);
        transfer.setTransFund(BigDecimal.valueOf(Long.parseLong(transferFund)));
        transfer.setCurrency(currency);
        //时间一到，执行任务
        mybankAPI.transferMoney(transfer);
        //执行完毕，删除任务
        scheduleJobService.delete(Long.parseLong(accOut));
    }
}
