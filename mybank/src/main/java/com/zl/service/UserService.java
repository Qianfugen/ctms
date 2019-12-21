package com.zl.service;

import com.zl.pojo.FenYe;
import com.zl.pojo.Transfer;
import com.zl.pojo.User;

import java.util.List;
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
     *
     * @param accNo
     * @return
     */
    User queryCustom(String accNo);

    /**
     * 根据用户卡号查询交易记录
     *
     * @param fenYe
     * @return
     */
    List<Transfer> queryTransferByAccNo(FenYe fenYe);

    /**
     * 注销登录
     */
    void logout();
}
