package com.zl.controller;

import com.zl.pojo.Account;
import com.zl.pojo.FenYe;
import com.zl.pojo.User;
import com.zl.service.ICustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ModelAndView allCustom(FenYe fenYe){
        ModelAndView mv=new ModelAndView();
        List<User> customs=cs.queryAllCustom(fenYe);
        System.out.println("******************用户所有信息***************"+customs.size());
        mv.addObject("customs",customs);
        mv.setViewName("allAccounts");
        return mv;
    }

    @RequestMapping("/updateCustom")
    public ModelAndView updateCustom(User user){
        ModelAndView mv=new ModelAndView();
        cs.updateCustom(user);
        System.out.println("******************修改用户信息***************");
        mv.setViewName("main");
        return mv;
    }

    @RequestMapping("/deleteCustom")
    public ModelAndView deleteCustom(String userId,FenYe fenYe){
        ModelAndView mv=new ModelAndView();
        cs.deleteCustom(userId);
        System.out.println("******************删除用户卡号信息***************");
        mv.setViewName("redirect:allCustom");
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
    public ModelAndView updateStatus(String userId,String accStatus,FenYe fenYe){
        ModelAndView mv=new ModelAndView();
        User user=new User();
        Account account=new Account();
        account.setAccStatus(new Integer(accStatus));
        user.setUserId(userId);
        user.setAccount(account);
        cs.updateStatus(user);
        System.out.println("******************修改用户状态***************");
        mv.setViewName("redirect:allCustom");
        return mv;
    }
    @RequestMapping("/toAllException")
    public ModelAndView toAllException(){
        ModelAndView mv=new ModelAndView();


        mv.setViewName("userDetails");
        return mv;
    }
    @RequestMapping("/toAllRecord")
    public ModelAndView toAllRecord(){
        ModelAndView mv=new ModelAndView();


        mv.setViewName("userDetails");
        return mv;
    }

}
