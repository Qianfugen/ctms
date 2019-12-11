package com.zl.controller;

import com.zl.pojo.Transfer;
import com.zl.service.PayeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主动收款的控制层
 * @author 徐浩杰
 * @version 1.0 2019-12-11
 */
@Controller
public class PayeeController {

    @Autowired
    private PayeeService ps;

    @RequestMapping("/doPayee")
    public ModelAndView doPayee(Transfer transfer){
        ModelAndView mv = new ModelAndView();
        ps.doPayee(transfer);
        return mv;
    }

}
