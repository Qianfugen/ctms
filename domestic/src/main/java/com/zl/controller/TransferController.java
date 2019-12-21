package com.zl.controller;

import com.zl.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author root
 */
@Controller
@RequestMapping("/transfer")
public class TransferController {
    @Autowired
    private TransferService transferService;

    /**
     * 根据卡号和用户名验证用户是否存在
     *
     * @param userName
     * @param accNo
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUser")
    public Map<String, Boolean> checkUser(@RequestParam("userName") String userName, @RequestParam("accNo") String accNo) {
        System.out.println("到达domestic的controller层。。。");
        Map<String, Boolean> map = new HashMap<>();
        System.out.println("userName:" + userName + " accNo:" + accNo);
        Boolean flag = transferService.checkUser(userName, accNo);
        //存在返回true,不存在返回false
        System.out.println("执行结果：" + flag);
        map.put("status", flag);
        return map;
    }
}
