package com.zl.dao;

import com.zl.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息操作
 * @author 徐浩杰
 * @version 1.0 2019-12-9
 */
public interface UserDao {

    /**
     * 验证客户号和客户姓名是否一致
     */
    User checkIdAndName(User user);

    /**
     * 根据卡号查用户
     */
    User queryNameByAccNo(String accNo);

}
