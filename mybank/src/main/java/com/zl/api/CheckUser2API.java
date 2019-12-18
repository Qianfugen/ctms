package com.zl.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@FeignClient("domestic-server")
public interface CheckUser2API {
    /**
     * 根据卡号和用户名验证用户是否存在
     *
     * @param userName
     * @param accNo
     * @return
     */
    @ResponseBody
    @RequestMapping("/transfer/checkUser")
    public Map<String, Boolean> checkUser(@RequestParam("userName") String userName, @RequestParam("accNo") String accNo);
}
