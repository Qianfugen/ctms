package com.zl.service;

import com.zl.pojo.User;

import java.util.Map;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-9
 */
public interface UserService {

    /**
     * 用户注册（修改密码为加密密码）
     *
     * @param accNo
     * @param password
     */
    void register(String accNo, String password);

    /**
     * 验证是否重复注册
     *
     * @return
     */
    Map<String, Object> regName(String accNo);

    /**
     * 根据卡号查询用户详细信息
     * @param accNo
     * @return
     */
    User queryCustom(String accNo);

    /**
     * 用户注销
     */
    void logout();
}
