package com.zl.dao;

import com.zl.pojo.*;

import java.util.List;

/**
 * @author junqi
 */
public interface ICustomDao {

    /**
     * 查询所有客户的信息
     * @param fenYe
     * @return
     */
    List<User> queryAllCustom(FenYe fenYe);

    /**
     * 查询客户信息符合要求的总条数
     * @param query
     * @return
     */
    int queryByLike(Query query);
    
    /**
     * 根据用户ID修改用户信息
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 根据用户ID修改用户卡信息
     * @param user
     * @return
     */
    int updateAccount(User user);

    /**
     * 根据卡号修改用户卡状态（冻结或启用）
     * @param user
     * @return
     */
    int updateStatus(User user);

    /**
     * 根据卡号删除用户卡
     * @param userId
     * @return
     */
    int deleteCustom(String userId);

    /**
     * 根据卡号查询用户详细信息
     * @param userId
     * @return
     */
    User queryCustom(String userId);

    /**
     * 查询所有交易记录
     * @param fenYe
     * @return
     */
    List<Transfer> queryAllTransfer(FenYe fenYe);

    /**
     * 查询交易记录符合要求的总条数
     * @param query
     * @return
     */
    List<Transfer> queryTransByLike(Query query);

    /**
     * 根据用户卡号查询交易记录
     * @param accNo
     * @return
     */
    List<Transfer> queryTransferByAccNo(String accNo);

    /**
     * 根据用户卡号查询登入记录
     * @param accNo
     * @return
     */
    List<Login> queryLoginByAccNo(String accNo);

    /**
     * 根据用户卡号查询登入异常记录
     * @param accNo
     * @return
     */
    List<Login> queryExLoginByAccNo(String accNo);

    /**
     * 根据用户卡号查询交易异常记录
     * @param accNo
     * @return
     */
    List<Transfer> queryExTransferByAccNo(String accNo);

}
