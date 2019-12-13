package com.zl.controller;

import com.zl.pojo.*;
import com.zl.service.ICustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 用户信息操作
 * @author junqi
 */
@Controller
@RequestMapping("/custom")
public class CustomController {

    @Autowired
    private ICustomService cs;

    @RequestMapping("/allCustom")
    public ModelAndView allCustom( FenYe fenYe){
        ModelAndView mv=new ModelAndView();
        List<User> customs=cs.queryAllCustom(fenYe);
        System.out.println(fenYe+"******************用户所有信息***************"+customs.size());
        mv.addObject("customs",customs);
        mv.setViewName("allAccounts");
        return mv;
    }

    @RequestMapping("/updateCustom")
    public ModelAndView updateCustom(User user){
        ModelAndView mv=new ModelAndView();
        System.out.println(user+"2222222222222222222222222222222222222222");
        int flag=cs.updateCustom(user);
        System.out.println(flag+"******************修改用户信息***************");
        if (flag==0){
            mv.addObject("message","修改失败，请核对信息！");
        }else {
            mv.addObject("message","修改成功！");
        }
        mv.setViewName("userDetails");
        return mv;
    }

    @RequestMapping("/deleteCustom")
    public ModelAndView deleteCustom(String userId,FenYe fenYe){
        ModelAndView mv=new ModelAndView();
        cs.deleteCustom(userId);
        System.out.println(fenYe+"******************删除用户卡号信息***************");
        mv.setViewName("allCustom");
        return mv;
    }

    @RequestMapping("/toInformation")
    public ModelAndView toInformation(String userId){
        ModelAndView mv=new ModelAndView();
        User user=cs.queryCustom(userId);
        System.out.println(user+"******************进入用户详情信息界面***************");
        mv.addObject("user",user);
        mv.setViewName("userDetails");
        return mv;
    }

    @RequestMapping("/updateStatus")
    public ModelAndView updateStatus(@RequestParam("userId") String userId,@RequestParam("accStatus") String accStatus,FenYe fenYe){
        ModelAndView mv=new ModelAndView();
        User user=new User();
        Account account=new Account();
        account.setAccStatus(new Integer(accStatus));
        user.setUserId(userId);
        user.setAccount(account);
        cs.updateStatus(user);
        System.out.println("******************修改用户状态***************");
        System.out.println(fenYe);
        mv.addObject("fenYe",fenYe);
        mv.setViewName("allCustom");
        return mv;
    }
    @RequestMapping("/toCustomRecord")
    public ModelAndView toCustomRecord(@RequestParam("accNo") String accNo){
        ModelAndView mv=new ModelAndView();
        User user=cs.queryCustom(accNo);
        System.out.println(user+"******************进入用户交易信息界面***************");
        mv.addObject("user",user);
        mv.setViewName("transactionRecord");
        return mv;
    }
    @RequestMapping("/toCustomLogin")
    public ModelAndView toCustomLogin(String accNo){
        ModelAndView mv=new ModelAndView();
        List<Login> loginList = cs.queryLoginByAccNo(accNo);
        System.out.println(loginList+"******************进入用户登入记录信息界面***************");
        if (loginList!=null){
            mv.addObject("loginList",loginList);
        }else {
            mv.addObject("message","该用户暂无登入异常记录");
        }
        mv.addObject("accNo",accNo);
        mv.setViewName("loginRecord");
        return mv;
    }
    @RequestMapping("/toCustomLogException")
    public ModelAndView toCustomLogException(@RequestParam("accNo") String accNo){
        ModelAndView mv=new ModelAndView();
        List<Login> loginList = cs.queryExLoginByAccNo(accNo);
        System.out.println(loginList+"******************进入用户登入异常信息信息界面***************");
        if (loginList!=null){
            mv.addObject("loginList",loginList);
        }else {
            mv.addObject("message","该用户暂无登入异常信息");
        }
        mv.addObject("accNo",accNo);
        mv.setViewName("exceptionRecord");
        return mv;
    }
    @RequestMapping("/toCustomTranException")
    public ModelAndView toCustomTranException(String accNo){
        ModelAndView mv=new ModelAndView();
        List<Transfer> TransList = cs.queryExTransferByAccNo(accNo);
        System.out.println(TransList+"******************进入用户交易异常信息信息界面***************");
        if(TransList!=null){
            mv.addObject("TransList",TransList);
        }else {
            mv.addObject("message","该用户暂无交易异常信息");
        }
        mv.addObject("accNo",accNo);
        mv.setViewName("exceptionRecord");
        return mv;
    }

    @RequestMapping("/toAllException")
    public ModelAndView toAllException(){
        ModelAndView mv=new ModelAndView();

        System.out.println("********************所有用户异常界面************************");
        mv.setViewName("userDetails");
        return mv;
    }
    @RequestMapping("/toAllRecord")
    public ModelAndView toAllRecord(FenYe fenYe){
        ModelAndView mv=new ModelAndView();
        List<Transfer> transfers=cs.queryAllTransfer(fenYe);
        System.out.println(fenYe+"********************所有用户交易记录界面************************");
        mv.addObject("transfers",transfers);
        mv.setViewName("totalTransactionRecord");
        return mv;
    }

}
