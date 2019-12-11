package com.zl.dao;

import com.zl.pojo.Transfer;

import java.math.BigDecimal;

/**
 * @author root
 */
public interface TransferDao {
    /**
     * 加钱
     *
     * @param transfer 交易对象
     * @return
     */
    public int addMoney(Transfer transfer);

    /**
     * 减钱
     *
     * @param transfer 交易对象
     * @return
     */
    public int subMoney(Transfer transfer);

    /**
     * 写入交易记录
     * @param transfer
     * @return
     */
    public int writeDeal(Transfer transfer);

    public BigDecimal queryBalance(String accNo);

    /**
     * 根据流水号查询交易记录
     *
     * @return 流水号
     */
    public Transfer queryTransferByDealNo(String dealNo);

}
