package com.zl.controller;

import com.zl.pojo.*;
import com.zl.service.PayeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 主动收款的控制层
 *
 * @author 徐浩杰
 * @version 1.0 2019-12-11
 */
@Controller
@RequestMapping("/payee")
public class PayeeController {

    @Autowired
    private PayeeService ps;

    /**
     * 处理催款
     *
     * @param transfer
     * @return
     */
    @RequestMapping("/doPayee")
    public ModelAndView doPayee(Transfer transfer) {
        ModelAndView mv = new ModelAndView();
        ps.doPayee(transfer);
        return mv;
    }

    /**
     * 去主动收款界面前查询出当前账户的借款人集合
     *
     * @param paging
     * @param session
     * @return
     */
    @RequestMapping("/toActiveCollection")
    public ModelAndView toActiveCollection(Paging paging, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String loginAccount = (String) session.getAttribute("loginAccount");
        System.out.println("当前账户" + loginAccount);

        if (paging.getQuery() != null) {
            paging.getQuery().setCreditorAcc(loginAccount);
        } else {
            Query query = new Query();
            query.setCreditorAcc(loginAccount);
            paging.setQuery(query);
        }
        List<Payee> payees = ps.queryPayeeByPaging(paging);
        mv.addObject("payees", payees);
        mv.setViewName("activeCollection");
        return mv;
    }

}
