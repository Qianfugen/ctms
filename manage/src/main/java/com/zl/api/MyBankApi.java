package com.zl.api;

import com.zl.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author junqi
 */
@FeignClient("mybank-server")
public interface MyBankApi {

    /**
     * 提供加密密码的接口
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/user/regUserPwd")
    public String regUserPwd(@RequestBody User user);
}
