package com.zl.service.impl;

import com.zl.dao.UserDao;
import com.zl.pojo.User;
import com.zl.service.UserService;
import com.zl.utils.EncryptionUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
     * 验证是否重复注册
     *
     * @return
     */
    public Map<String, Object> regName(String accNo) {
        Map<String, Object> result = new HashMap<String, Object>();
        User user = ud.queryCustom(accNo);
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
        User user = ud.queryCustom(accNo);
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        Map<String, String> encrypt = encryptionUtil.encryption(accNo, password);
        user.setUserPwd(encrypt.get("password"));
        ud.updateUserPwd(user);
        System.out.println("加密password: " + user.getUserPwd());
    }

    /**
     * 根据卡号查询用户详细信息
     *
     * @param accNo
     * @return
     */
    @Override
    public User queryCustom(String accNo) {
        return ud.queryCustom(accNo);
    }

    /**
     * 用户注销
     */
    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
        }
    }
}
