package com.zl.service;

import com.zl.pojo.Paging;
import com.zl.pojo.PayInfo;
import com.zl.pojo.Payee;
import com.zl.pojo.Transfer;
import org.springframework.transaction.annotation.Transactional;

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
     * 处理还款
     * @param transfer
     */
    void doPayee(Transfer transfer);
}
