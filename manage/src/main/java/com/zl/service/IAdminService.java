package com.zl.service;

import com.zl.pojo.Admin;

/**
 * @author junqi
 */
public interface IAdminService {

    /**
     * 验证管理员登入接口
     *
     * @param admin
     * @return
     */
    Admin loginAdmin(Admin admin);

    /**
     * 修改管理员密码接口
     *
     * @param admin
     * @return
     */
    int updateAdminPwd(Admin admin);
}
