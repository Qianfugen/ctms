package com.zl.controller;

import com.zl.pojo.PayInfo;
import com.zl.service.PayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 消息通知控制层
 *
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
@Controller
@RequestMapping("payInfo")
public class PayInfoController {
    @Autowired
    private PayInfoService pis;

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
     * 去催款通知前查询所有的催款通知
     *
     * @return
     */
    @RequestMapping("/toPayInfoMessage")
    public ModelAndView toPayInfoMessage() {
        ModelAndView mv = new ModelAndView();

        return mv;

    }
}
