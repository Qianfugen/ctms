package com.zl.dao;

import com.zl.pojo.FenYe;
import com.zl.pojo.Query;
import com.zl.pojo.Transfer;
import com.zl.pojo.User;

import java.util.List;

/**
 * 用户信息操作
 *
 * @author 徐浩杰
 * @version 1.0 2019-12-9
 */
public interface UserDao {
    /**
     * （注册）修改用户登录密码为加密密码
     */
    int updateUserPwd(User user);


    /**
     * 根据卡号查询用户详细信息
     * @param accNo
     * @return
     */
    User queryCustom(String accNo);

    /**
     * 根据用户卡号查询交易记录
     * @param fenYe
     * @return
     */
    List<Transfer> queryTransferByAccNo(FenYe fenYe);

    /**
     * 根据用户卡号查询交易记录
     * @param query
     * @return
     */
    int queryTransByLike(Query query);

}
