package com.zl.dao;

import com.zl.pojo.Transfer;
import com.zl.pojo.User;

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
     *
     * @param transfer
     * @return
     */
    public int writeDeal(Transfer transfer);

    /**
     * 根据卡号查询余额
     *
     * @param accNo 卡号
     * @return
     */
    public BigDecimal queryBalance(String accNo);

    /**
     * 根据卡号查询银行
     *
     * @param accNo 卡号
     * @return
     */
    public String queryBankName(String accNo);

    /**
     * 根据卡号查询用户名
     *
     * @param accNo 卡号
     * @return
     */
    public String queryUserName(String accNo);

    /**
     * 根据用户名和卡号判断是否存在
     *
     * @param user 用户对象
     * @return
     */
    public User checkUser(User user);

    /**
     * 根据流水号查询交易记录
     *
     * @return 流水号
     */
    public Transfer queryTransferByDealNo(String dealNo);

}
