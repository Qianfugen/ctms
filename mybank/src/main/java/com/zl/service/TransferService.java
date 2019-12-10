package com.zl.service;

import com.zl.pojo.Transfer;

/**
 * 转账service层接口
 * @author root
 */
public interface TransferService {
    /**
     * 同行转账
     * @param transfer 交易对象
     * @return
     */
    public void transferMoney(Transfer transfer);

    /**
     * 根据流水号查询交易记录
     *
     * @return 流水号
     */
    public Transfer queryTransferByDealNo(String dealNo);
}
