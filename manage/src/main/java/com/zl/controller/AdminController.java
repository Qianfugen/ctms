package com.zl.controller;

import com.zl.pojo.Admin;
import com.zl.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 管理员账号操作
 *
 * @author junqi
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService as;

    /**
     * 管理员登入请求
     */
    @RequestMapping("/login")
    public ModelAndView loginAdmin(Admin admin, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        admin = as.loginAdmin(admin);
        System.out.println(admin + "**************登入用户***************");
        if (admin != null) {
            session.setAttribute("loginUser", admin);
            mv.addObject("message", "欢迎您再次登入");
            mv.setViewName("managerMain");
        } else {
            mv.addObject("message", "用户名或密码错误，请重新登入");
            mv.setViewName("redirect:toLogin");
        }
        return mv;
    }

    /**
     * 进入管理员登入页面请求
     */
    @RequestMapping("/toLogin")
    public ModelAndView toLoginAdmin(String message) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", message);
        mv.setViewName("Systemlogin");
        return mv;
    }

    /**
     * 进入修改管理员密码请求
     */
    @RequestMapping("/toUpdateAdmin")
    public ModelAndView toUpdateAdmin(String message) {
        ModelAndView mv = new ModelAndView();
        if (message != null && !"".equals(message)) {
            mv.addObject("message", message);
        }
        mv.setViewName("systemChangePasswd");
        return mv;
    }

    /**
     * 退出登入请求
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        session.invalidate();
        mv.setViewName("redirect:toLogin");
        return mv;
    }

    /**
     * 修改管理员密码请求
     */
    @RequestMapping("/updateAdminPwd")
    @Transactional
    public ModelAndView updateAdminPwd(Admin admin, @RequestParam("password1") String password1, @RequestParam("password2") String password2) {
        ModelAndView mv = new ModelAndView();
        if (as.loginAdmin(admin) != null) {
            if (!"".equals(password2) && password2 != null && password2.equals(password1)) {
                admin.setPassword(password1);
                System.out.println(admin + "****************修改密码*******************");
                int flag = as.updateAdminPwd(admin);
                mv.addObject("message", "修改密码成功，请重新登入！");
                mv.setViewName("redirect:toLogin");
            } else {
                mv.addObject("message", "修改失败！请重新输入");
                mv.setViewName("redirect:toUpdateAdmin");
            }
        } else {
            mv.addObject("message", "修改失败！请重新输入");
            mv.setViewName("redirect:toUpdateAdmin");
        }
        return mv;
    }
}
