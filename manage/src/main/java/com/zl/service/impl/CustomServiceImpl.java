package com.zl.service.impl;

import com.zl.dao.ICustomDao;
import com.zl.pojo.User;
import com.zl.service.ICustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author junqi
 */
@Service
public class CustomServiceImpl implements ICustomService {

    @Autowired
    private ICustomDao cd;

    @Override
    public List<User> queryAllCustom() {
        return cd.queryAllCustom();
    }
}
