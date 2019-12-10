package com.zl.dao;

import com.zl.pojo.User;

import java.util.List;

/**
 * @author junqi
 */
public interface ICustomDao {

    /**
     * 查询所有客户的信息
     * @return
     */
    List<User> queryAllCustom();

}
