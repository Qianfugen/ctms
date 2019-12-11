package com.zl.service;

import com.zl.pojo.Account;
import com.zl.pojo.Coll;
import com.zl.pojo.Transfer;

import java.util.List;

/**
 * @author fm
 * @version 1.0
 * @date 2019/12/10 12:48
 */
public interface CashSweepService {
    /**
     * 签约时，查询需要签约作为主账号的账户的签约状态，为“已签约”则不可作为主账户签约
     * @param accNo 传入需要作为主账户的账号
     * @return 返回该账户的签约状态
     */
    String queryCollStatus(String accNo);
    /**
     * 资金归集功能签约
     * @param account 需要签约为副卡的账号
     * @param coll 签约信息
     * @param collStatus 主卡的签约状态
     * @param signFund 签约金额
     * @return 返回签约结果 0失败 1成功
     */
    int signColl(Account account,Coll coll,String collStatus,String signFund);

    /**
     * 资金归集功能解约
     * @param viceAccount 需要解约的副卡账号
     * @param mainAccount 主卡账号
     * @return 返回解约结果 0成功 1 失败
     */
    int cancelColl(Account viceAccount,Account mainAccount);

    /**
     * 修改副卡的归集信息
     * @param reviseColl 传入修改后的归集信息
     * @param viceAccount 子账号
     * @param signFund 签约金额的字符串
     * @param collStatus 修改后的主账户的签约状态
     * @return 返回归集信息修改结果，大于0成功
     */
    int updateColl(Coll reviseColl,Account viceAccount,String signFund,String collStatus);

    /**
     * 查询副卡的归集信息
     * @param account 传入账号信息（副卡）
     * @return 返回当前副卡的归集信息
     */
    Coll queryColl(Account account);

    /**
     * 查询主卡的归集信息（子卡信息）
     * @param account 传入账号信息（主卡）
     * @return 返回主卡下的所有子卡的归集信息
     */
    List<Coll> queryMainColl(Account account);

    /**
     * 查询库中所有的归集信息
     * @return 返回所有的归集信息
     */
    List<Coll> queryAllCollInTable();

    /**
     * 查询归集记录
     * @param account 传入副卡的信息，以账号和交易类型作为查询条件
     * @return 返回归集转账交易记录
     */
    List<Transfer> queryTransfers(Account account);

    /**
     * 资金归集
     * @param colls 归集信息
     */
    void sweepCash(List<Coll> colls);
}
