package com.zl.service.impl;

import com.zl.dao.PayeeDao;
import com.zl.dao.UserDao;
import com.zl.pojo.Paging;
import com.zl.pojo.Payee;
import com.zl.pojo.User;
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
    @Autowired
    private UserDao ud;


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
        for (Payee p : payees) {
            User u1 = ud.queryUserByAccNo(p.getCreditorAcc());
            p.setCreditorName(u1.getUserName());
            User u2 = ud.queryUserByAccNo(p.getDebtor());
            p.setDebtorName(u2.getUserName());
            if(p.getCreditorName().equals("") || p.getCreditorName().equals("")){
                pd.updatePayee(p);
            }
        }
        return payees;
    }

    /**
     * 根据贷方和借方账户查询出payee
     *
     * @param payee
     */
    @Override
    public Payee queryPayee(Payee payee) {

        return pd.queryPayee(payee);
    }


}
