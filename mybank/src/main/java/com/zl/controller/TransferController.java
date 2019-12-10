package com.zl.controller;

import com.zl.pojo.Transfer;
import com.zl.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 同行转账
     *
     * @param transfer
     * @return
     */
    @ResponseBody
    @RequestMapping("/transferMoney")
    public Map<String, Integer> subMoney(@RequestBody Transfer transfer) {
        Map<String, Integer> map = new HashMap<>();
        transferService.transferMoney(transfer);
        map.put("status",200);
        return map;
    }

    @ResponseBody
    @RequestMapping("/queryTransferByDealNo")
    public Transfer queryTransferByDealNo(@RequestParam("dealNo") String dealNo) {
        System.out.println(dealNo);
        Transfer transfer = transferService.queryTransferByDealNo(dealNo);
        System.out.println(transfer);
        return transfer;
    }


}
