package com.zl.service.impl;

import com.zl.dao.PayeeDao;
import com.zl.pojo.Paging;
import com.zl.pojo.Payee;
import com.zl.pojo.Transfer;
import com.zl.service.PayeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 徐浩杰
 * @version 1.0 2019-12-10
 */
@Service
public class PayeeServiceImpl implements PayeeService {

    @Autowired
    private PayeeDao pd;


    /**
     * 分页查询出贷方信息
     */
    @Override
    public List<Payee> queryPayeeByPaging(Paging paging) {
        //设置符合要求的记录总数
        paging.setRowsCount(pd.queryPayeeCount(paging.getQuery()));

        /**
         * 处理分页对象
         */
        //设置当前页码
        if (paging.getCurrentPage() != null) {
            if (paging.getCurrentPage() <= 0) {
                paging.setCurrentPage(1);
            }
            //如果大于最大页数
            if (paging.getCurrentPage() > paging.getPages()) {
                paging.setCurrentPage(paging.getPages());
            }
        } else {
            paging.setCurrentPage(1);
        }

        List<Payee> payees = pd.queryPayeeByPaging(paging);
        return payees;
    }




}
