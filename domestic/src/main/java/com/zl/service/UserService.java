package com.zl.service;

import com.zl.pojo.User;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-9
 */
public interface UserService {

    /**
     * 根据卡号查用户
     */
    User queryUserByAccNo(String accNo);

}
