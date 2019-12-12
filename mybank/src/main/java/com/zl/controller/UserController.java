package com.zl.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录注册控制层
 *
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService us;

    /**
     * 注册(卡号)
     *
     * @return
     */
    @RequestMapping("/register")
    public ModelAndView register(String accNo, String password) {
        ModelAndView mv = new ModelAndView();
        if (accNo != null && !accNo.equals("") && password != null && !password.equals("")) {
            us.register("accNo", "password");
            mv.setViewName("login");
        } else {
            mv.addObject("error", "请填写注册信息");
            mv.setViewName("register");
        }
        return mv;
    }

    /**
     *ajax后台检测是否重复注册
     * @return false 重复 true 合法
     */
    @ResponseBody
    @RequestMapping("/regName")
    public Map<String, Object> regName(String accNo) {
        return us.regName(accNo);
    }


}
