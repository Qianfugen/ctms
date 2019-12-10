package com.zl.controller;

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
    public ModelAndView allCustom(){
        ModelAndView mv=new ModelAndView();
        List<User> list=cs.queryAllCustom();
        System.out.println(list.get(1)+"******************用户所有信息***************");
        mv.setViewName("main");
        return mv;
    }

}
