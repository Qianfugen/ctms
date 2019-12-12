package com.zl.dao;

import com.zl.pojo.FenYe;
import com.zl.pojo.Query;
import com.zl.pojo.User;

import java.util.List;

/**
 * @author junqi
 */
public interface ICustomDao {

    /**
     * 查询所有客户的信息
     * @param fenYe
     * @return
     */
    List<User> queryAllCustom(FenYe fenYe);

    /**
     * 查询符合要求的总条数
     * @param query
     * @return
     */
    int queryByLike(Query query);
    /**
     * 根据卡号修改用户卡状态（冻结或启用）
     * @param user
     * @return
     */
    int updateCustom(User user);
    /**
     * 根据卡号修改用户卡状态（冻结或启用）
     * @param user
     * @return
     */
    int updateStatus(User user);

    /**
     * 根据卡号删除用户卡
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

}
