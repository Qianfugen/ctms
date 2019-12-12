package com.zl.service;

import com.zl.pojo.FenYe;
import com.zl.pojo.Query;
import com.zl.pojo.User;

import java.util.List;

/**
 * @author junqi
 */
public interface ICustomService {

    /**
     * 查询所有客户的信息
     * @param fenYe
     * @return
     */
    List<User> queryAllCustom(FenYe fenYe);

    /**
     * 根据卡号修改用户卡状态（冻结或启用）
     * @param user
     * @return
     */
    int updateCustom(User user);

    /**
     * 根据用户id删除用户卡
     * @param userId
     * @return
     */
    int deleteCustom(String userId);

    /**
     * 根据卡号查询用户详细信息
     * @param userId
     * @return
     */
    User queryCustom(String userId);

    /**
     * 根据卡号修改用户卡状态（冻结或启用）
     * @param user
     * @return
     */
    int updateStatus(User user);
}
