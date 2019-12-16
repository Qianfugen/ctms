package com.quart.job.api;


import com.quart.job.entity.Transfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@FeignClient("mybank-server")
public interface MybankAPI {
    /**
     * 同行转账
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/transfer/transferMoney")
    public Map<String, Integer> transferMoney(@RequestBody Transfer transfer);
}
