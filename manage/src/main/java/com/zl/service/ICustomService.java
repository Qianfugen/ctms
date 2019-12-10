package com.zl.service;

import com.zl.pojo.User;

import java.util.List;

/**
 * @author junqi
 */
public interface ICustomService {

    /**
     * 查询所有客户的信息
     * @return
     */
    List<User> queryAllCustom();
}
