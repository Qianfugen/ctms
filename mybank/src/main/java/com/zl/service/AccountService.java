package com.zl.service;

import com.zl.pojo.Account;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
public interface AccountService {
    /**
     * 根据卡号查询account对象
     */
    Account queryAccountByAccNo(String accNo);
}
