package com.zl.service.impl;

import com.zl.dao.AccountDao;
import com.zl.pojo.Account;
import com.zl.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao ad;

    /**
     * 根据卡号查询account对象
     */
    @Override
    public Account queryAccountByAccNo(String accNo) {
        return ad.queryAccountByAccNo(accNo);
    }
}
