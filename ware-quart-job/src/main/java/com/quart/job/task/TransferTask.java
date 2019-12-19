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
        System.out.println("定时任务开始执行。。。");
        String[] bankInfo = params.split(",");
        String accIn = bankInfo[0];
        String accOut = bankInfo[1];
        String transferFund = bankInfo[2];
        String currency = bankInfo[3];
        String fee = bankInfo[4];
        System.out.println("accIn:" + accIn);
        System.out.println("accOut:" + accOut);
        System.out.println("transferFund:" + transferFund);
        System.out.println("currency:" + currency);
        System.out.println("fee" + fee);
        Transfer transfer = new Transfer();
        transfer.setAccIn(accIn);
        transfer.setAccOut(accOut);
        transfer.setTransFund(new BigDecimal(transferFund));
        transfer.setCurrency(currency);
        transfer.setFee(new BigDecimal(fee));
        System.out.println("定时任务的transfer:" + transfer);
        //时间一到，执行任务
        if (accIn.matches("^622230.*")) {
            mybankAPI.transferMoney(transfer);
        } else {
            mybankAPI.transferMoneyDemo(transfer);
        }
        //执行完毕，删除任务
        scheduleJobService.delete(Long.parseLong(accOut));
        System.out.println("交易已完成。。。");
    }
}
