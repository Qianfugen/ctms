package com.zl.controller;


import com.zl.pojo.*;
import com.zl.service.PayInfoService;
import com.zl.service.PayeeService;
import com.zl.service.TransferService;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private UserService us;
    @Autowired
    private PayInfoService pis;


    /**
     * 去主动收款界面前查询出当前账户的借款人集合
     *
     * @param paging
     * @param session
     * @return
     */
    @RequestMapping("/activeCollection")
    public ModelAndView activeCollection(Paging paging, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String loginAccNo = (String) session.getAttribute("loginAccNo");
        System.out.println("当前账户" + loginAccNo);
        if (paging.getQuery() != null) {
            paging.getQuery().setCreditorAcc(loginAccNo);
        } else {
            Query query = new Query();
            query.setCreditorAcc(loginAccNo);
            paging.setQuery(query);
        }
        List<Payee> payees = ps.queryPayeeByPaging(paging);
        mv.addObject("payees", payees);
        mv.setViewName("activeCollection");
        return mv;
    }

    /**
     * 批量发送消息通知
     *
     * @return
     */
    @RequestMapping("/addPayInfos")
    public ModelAndView addPayInfos(HttpSession session, String[] debtor) {
        System.out.println("进入发消息   ：");
        ModelAndView mv = new ModelAndView();
        String loginAccNo = (String) session.getAttribute("loginAccNo");
        //查询出当前登录卡的用户
        User loginUser = us.queryCustom(loginAccNo);
        int index = 0;//成功的条数

        for (int i = 0; i < debtor.length; i++) {
            System.out.println("debtor[i] " + debtor[i]);
            PayInfo payInfo = new PayInfo();
            Payee payee = new Payee();
            payee.setCreditorAcc(loginAccNo);
            payee.setDebtor(debtor[i]);

            payInfo.setCreditorAcc(loginAccNo);
            payInfo.setDebtor(debtor[i]);
            payInfo.setFund(ps.queryPayee(payee).getFund());
            payInfo.setInfoTime(new Date());
            payInfo.setCreditorName(loginUser.getUserName());
            payInfo.setDebtorName(us.queryCustom(debtor[i]).getUserName());
            if (pis.queryPayInfo(payInfo) == null) {
                index = pis.addPayInfo(payInfo);
            } else {
                System.out.println("有重复催款通知发送");
            }
        }

        if (index != 0) {
            /**
             * 后续可以设置为前端提示发送消息成功
             */
            System.out.println("批量执行成功");
        }
        mv.setViewName("/payee/activeCollection");
        return mv;
    }
}
