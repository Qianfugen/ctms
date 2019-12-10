package com.zl.controller;

import com.zl.pojo.Admin;
import com.zl.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author junqi
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService as;

    @RequestMapping("/login")
    public ModelAndView loginAdmin(Admin admin){
        System.out.println(admin+"*****************************");
        ModelAndView mv=new ModelAndView();
        admin=as.loginAdmin(admin);
        System.out.println(admin+"*****************************");
        if (admin!=null){
            mv.setViewName("adminMain");
        }else {
            mv.addObject("error","用户名或密码错误，请重新登入");
            mv.setViewName("redirect:toLogin");
        }
        return mv;
    }

    @RequestMapping("/toLogin")
    public ModelAndView toLoginAdmin(String error){
        ModelAndView mv=new ModelAndView();
        mv.addObject("error",error);
        mv.setViewName("login");
        return mv;
    }
}
