package com.zl.dao;

import com.zl.pojo.Paging;
import com.zl.pojo.Payee;
import com.zl.pojo.Query;

import java.util.List;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
public interface PayeeDao {

    /**
     * 分页查询所有的贷方信息
     * @return
     */
     List<Payee> queryPayeeByPaging(Paging paging);

    /**
     * 查询符合条件的贷方记录数
     * @param query 查询条件
     * @return 符合条件记录数
     */
     int queryPayeeCount(Query query);

    /**
     * 根据贷方和借方账户查询出payee
     */
    Payee queryPayee(Payee payee);


}
