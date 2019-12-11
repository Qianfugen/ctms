package com.zl.dao;

import com.zl.pojo.Account;
import com.zl.pojo.Coll;
import com.zl.pojo.Transfer;

import java.util.List;

/**
 * @author fm
 * @version 1.0
 * @date 2019/12/9 20:31
 */
public interface CashSweepDao {
    /**
     * 查询签约状态
     * @param accNo 传入需要需要查询的账号
     * @return 返回该账户的签约状态
     */
    String queryCollStatus(String accNo);
    /**
     * 更新账号归集签约状态,需要和归集信息相关的操作一起执行
     * @param account 传入账号信息，账号作为定位条件，将归集状态更改为“已签约”、“未签约”和“主账号”中的一个
     *                主账号更改时，只能更改为“未签约”
     * @return 返回更新结果，大于0成功
     */
    int updateCollStatus(Account account);

    /**
     * 添加归集表信息，调用这个方法的同时需要调用updateCollStatus方法更新账号签约状态为"已签约"
     * @param coll 归集信息对象传入
     * @return 返回添加结果，大于0成功
     */
    int addColl(Coll coll);

    /**
     * 查询副卡的归集信息
     * @param account 传入账号信息（副卡）
     * @return 返回当前副卡的归集信息
     */
    Coll queryColl(Account account);

    /**
     * 修改副卡的归集信息
     * @param coll 传入修改后的归集信息
     * @return 返回归集信息修改结果，大于0成功
     */
    int updateColl(Coll coll);

    /**
     * 删除签约信息，删除签约信息的同时需要用updateCollStatus方法更新签约状态为“未签约”
     * @param account 传入归集的子账号，按照子账号来删除，子账号唯一（即一张卡不能归集到多张卡）
     * @return 返回删除结果，大于0成功
     */
    int deleteColl(Account account);

    /**
     * 查询主卡的归集信息（子卡信息）
     * @param account 传入账号信息（主卡）
     * @return 返回主卡下的所有子卡的归集信息
     */
    List<Coll> queryMainColl(Account account);

    /**
     * 查询归集记录
     * @param account 传入副卡的信息，以账号和交易类型作为查询条件
     * @return 返回归集转账交易记录
     */
    List<Transfer> queryTransfers(Account account);

    /**
     * 查询库中所有的归集信息
     * @return 返回所有的归集信息
     */
    List<Coll> queryAllCollInTable();
}
