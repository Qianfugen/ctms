package com.zl.service;

import com.zl.pojo.Transfer;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 转账service层接口
 *
 * @author root
 */
public interface TransferService {
    /**
     * 同行转账
     *
     * @param transfer 交易对象
     * @return
     */
    public void transferMoney(Transfer transfer);

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
     * 根据流水号查询交易记录
     *
     * @return 流水号
     */
    public Transfer queryTransferByDealNo(String dealNo);
}
