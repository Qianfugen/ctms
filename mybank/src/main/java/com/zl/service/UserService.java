package com.zl.service;

import com.zl.pojo.User;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-9
 */
public interface UserService {
    /**
     *验证卡号和姓名合法性
     */
    public String checkIdAndName(User user);
}
