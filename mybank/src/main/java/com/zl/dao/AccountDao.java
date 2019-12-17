package com.zl.dao;

import com.zl.pojo.Account;
import org.springframework.stereotype.Component;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
public interface AccountDao  {
    /**
     * 根据卡号查询account对象
     */
    Account queryAccountByAccNo(String accNo);
}
