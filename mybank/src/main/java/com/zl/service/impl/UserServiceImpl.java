package com.zl.service.impl;

import com.zl.dao.UserDao;
import com.zl.pojo.User;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
