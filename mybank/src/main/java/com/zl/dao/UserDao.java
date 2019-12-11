package com.zl.dao;

import com.zl.pojo.User;

/**
 * 用户信息操作
 * @author 徐浩杰
 * @version 1.0 2019-12-9
 */
public interface UserDao {

    /**
     * 验证客户号和客户姓名是否一致
     */
    User checkIdAndName(User user);

    /**
     * 根据卡号查姓名
     */
    String queryNameByAccNo(String accNo);

}
