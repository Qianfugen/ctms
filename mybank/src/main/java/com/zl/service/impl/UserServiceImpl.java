package com.zl.service.impl;

import com.zl.dao.UserDao;
import com.zl.pojo.User;
import com.zl.service.UserService;
import com.zl.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao ud;


    /**
     * 根据卡号查用户
     *
     * @param accNo
     */
    @Override
    public User queryUserByAccNo(String accNo) {
        return ud.queryUserByAccNo(accNo);
    }

    /**
     * 验证是否重复注册
     *
     * @return
     */
    public Map<String, Object> regName(String accNo) {
        Map<String, Object> result = new HashMap<String, Object>();
        User user = ud.queryUserByAccNo(accNo);
        if (user.getUserPwd() != null) {
            //重复注册
            result.put("flag", false);
        } else {
            //第一次注册
            result.put("flag", true);
        }
        return result;
    }

    /**
     * 用户注册（修改密码为加密密码）
     *
     * @param accNo
     * @param password
     */
    @Override
    public void register(String accNo, String password) {
        System.out.println("进入登录service");
        User user = ud.queryUserByAccNo(accNo);
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        Map<String, String> encrypt = encryptionUtil.encryption(accNo, password);
        user.setUserPwd(encrypt.get("password"));
        ud.updateUserPwd(user);
        System.out.println("加密password: " + user.getUserPwd());
    }
}
