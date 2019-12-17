package com.zl.service;

import com.zl.pojo.Transfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 转账service层接口
 *
 * @author root
 */
public interface TransferService {
    /**
     * 定时任务
     */
    public int executeJob(Transfer transfer);

    /**
     * 同行转账
     *
     * @param transfer 交易对象
     * @return
     */
    public void transferMoney(Transfer transfer);

    /**
     * 跨境转账
     *
     * @param transfer
     */
    public void transferMoneyOver(Transfer transfer);

    /**
     * 写入交易记录
     *
     * @param transfer
     * @return
     */
    public int writeDeal(Transfer transfer);

    /**
     * 根据卡号查询余额
     *
     * @param accNo 卡号
     * @return 余额
     */
    public BigDecimal queryBalance(String accNo);

    /**
     * 根据卡号查询用户名和银行
     *
     * @param accNo 卡号
     * @return
     */
    public Map<String, String> queryBankAndUserName(String accNo);


    /**
     * 根据卡号和用户名验证用户是否存在
     *
     * @param userName 用户名
     * @param accNo    卡号
     * @return
     */
    public Boolean checkUser(String userName, String accNo);

    /**
     * 根据流水号查询交易记录
     *
     * @return 流水号
     */
    public Transfer queryTransferByDealNo(String dealNo);

    /**
     * 根据流水号查询未完成的记录
     *
     * @param dealNo
     * @return
     */
    public Transfer queryTransferDealing(String dealNo);

    /**
     * 流水记录处理成功
     *
     * @param dealNo
     * @return
     */
    public int transferConfirm(String dealNo);

    /**
     * 自动把未完成的记录发送到消息队列
     */
    public void autoSend();

    /**
     * 查询最大上限
     *
     * @param accNo
     * @return
     */
    public BigDecimal queryAccLimit(String accNo);

    /**
     * 查询启用状态
     *
     * @param accNo
     * @return
     */
    public int queryAccStatus(String accNo);

    /**
     * 转账前的验证
     *
     * @param transfer
     * @param bank
     * @return
     */
    public Map<String, Integer> verifyTransfer(Transfer transfer, String bank);
}
