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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
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

    @Autowired(required = false)
    private PayeeService ps;
    @Autowired
    private UserService us;
    @Autowired
    private PayInfoService pis;
    @Autowired
    private TransferService ts;

    /**
     * 处理催款
     *
     * @param payInfo
     * @return
     */
    @RequestMapping("/doPayee")
    public ModelAndView doPayee(PayInfo payInfo) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("payInfo");
        mv.setViewName(""); //待定;通知内容的value设为payinfo.xx
        return mv;
    }

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
        session.setAttribute("payees", payees);
        mv.setViewName("activeCollection");
        return mv;
    }

    /**
     * 批量发送消息通知
     * 全选提交实现：
     * 一：提交对象 二：value=对象
     *
     * @return
     */
    @RequestMapping("/addPayInfos")
    public ModelAndView addPayInfos(HttpSession session, @RequestBody List<Payee> payees) {
        ModelAndView mv = new ModelAndView();
        String loginAccNo = (String) session.getAttribute("loginAccNo");
        //查询出当前登录卡的用户
        User loginUser = us.queryUserByAccNo(loginAccNo);
        int index = 0;//成功的条数
        for (Payee p : payees) {
            PayInfo payInfo = new PayInfo();
            payInfo.setCreditorAcc(loginAccNo);
            payInfo.setDebtor(p.getDebtor());
            payInfo.setFund(p.getFund());
            payInfo.setInfoTime(new Date());
            payInfo.setCreditorName(loginUser.getUserName());
            payInfo.setDebtorName(p.getDebtorName());
            index = pis.addPayInfo(payInfo);
        }
        if (index != 0) {
            /**
             * 后续可以设置为前端提示发送消息成功
             */
            System.out.println("批量执行成功");
        }
        mv.setViewName("/activeCollection");
        return mv;
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
