package com.zl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice(basePackages = "com.zl")
public class ErrorController {
    @ExceptionHandler
    public ModelAndView error(Exception e) {
        ModelAndView mv = new ModelAndView();
        System.out.println("出错啦！");
        e.printStackTrace();
        mv.addObject("msg", e.getMessage());
        mv.setViewName("error");
        return mv;
    }
}