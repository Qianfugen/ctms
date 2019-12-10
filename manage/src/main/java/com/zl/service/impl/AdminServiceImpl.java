package com.zl.service.impl;

import com.zl.dao.IAdminDao;
import com.zl.pojo.Admin;
import com.zl.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IAdminDao ad;
    @Override
    public Admin loginAdmin(Admin admin) {
        return ad.loginAdmin(admin);
    }

    @Override
    public int updateAdminPwd(Admin admin) {
        return ad.updateAdminPwd(admin);
    }
}
