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
     * 验证卡号和客户名合法性
     * 合法则msg=""
     */
    @Override
    public String checkIdAndName(User user) {
        String msg = "";
        User u = ud.checkIdAndName(user);
        //卡号不存在
        if (u == null) {
            msg = "该客户号不存在";
            return msg;
        }
        //卡号和姓名不一致
        if (!u.getUserName().equals(u.getUserName())) {
            msg = "客户号和客户名不一致";
            return msg;
        }
        return msg;
    }

    /**
     * 根据卡号查用户
     * @param accNo
     */
    @Override
    public User queryNameByAccNo(String accNo) {
        return ud.queryNameByAccNo(accNo);
    }

    /**
     * 验证是否重复注册
     *
     * @return
     */
    public Map<String, Object> regName(String accNo) {
        Map<String, Object> result = new HashMap<String, Object>();
        User u = ud.queryNameByAccNo(accNo);
        if (u.getUserPwd() != null) {
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
        User user = ud.queryNameByAccNo(accNo);
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        Map<String ,String> encrypt = encryptionUtil.encryption(password);
        user.setUserPwd(encrypt.get("password"));
    }
}
