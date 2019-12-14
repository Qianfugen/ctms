package com.zl.service;


import com.zl.pojo.Paging;
import com.zl.pojo.Payee;

import java.util.List;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
public interface PayeeService {
    /**
     * 分页查询出贷方信息
     * @param paging
     * @return
     */
    List<Payee> queryPayeeByPaging(Paging paging);

    /**
     * 根据贷方和借方账户查询出payee
     */
    Payee queryPayee(Payee payee);

}
