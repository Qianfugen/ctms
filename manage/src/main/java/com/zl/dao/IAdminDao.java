package com.zl.dao;

import com.zl.pojo.Admin;

/**
 * @author junqi
 */
public interface IAdminDao {

    /**
     * 验证管理员登入
     * @param admin
     * @return
     */
    Admin loginAdmin(Admin admin);
}
