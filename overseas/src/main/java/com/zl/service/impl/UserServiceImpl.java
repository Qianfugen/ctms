package com.zl.service.impl;

import com.zl.dao.UserDao;
import com.zl.pojo.User;
import com.zl.service.UserService;
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

}
