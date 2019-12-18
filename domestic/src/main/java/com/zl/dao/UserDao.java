package com.zl.dao;

import com.zl.pojo.User;

/**
 * 用户信息操作
 *
 * @author 徐浩杰
 * @version 1.0 2019-12-9
 */
public interface UserDao {

    /**
     * 根据卡号查用户
     */
    User queryUserByAccNo(String accNo);

}
