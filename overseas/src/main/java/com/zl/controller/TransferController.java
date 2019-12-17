package com.zl.controller;

import com.zl.pojo.Transfer;
import com.zl.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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

//    /**
//     * 分类转账
//     *
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/sortTransfer")
//    public Map<String, Integer> sortTransfer(Transfer transfer, HttpSession session, @RequestParam("bank") String bank) {
//        //预先设置一个转出账户，本应从页面获取，这里仅做测试
////        session.getAttribute("loginUser");
//        transfer.setAccOut("6222303626811324642");
//        Map<String, Integer> map = new HashMap<>();
//        if ("0".equals(bank)) {
//            //境内转账
//            String banktype=transfer.getAccIn().substring(0,6);
//            System.out.println("银行前六位："+banktype);
//            if("622230".equals(banktype)){
//                //同行转账
//                transferService.executeJob(transfer);
//                map.put("status", 200);
//            }else{
//                //跨行转账
//                transferService.executeJob(transfer);
//                map.put("status", 200);
//            }
//        } else {
//            System.out.println("跨境转账。。。");
//            transferService.transferMoneyOver(transfer);
//            map.put("status", 200);
//        }
//        return map;
//
//    }
//
//    /**
//     * 同行转账
//     *
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/transferMoney")
//    public Map<String, Integer> transferMoney(@RequestBody Transfer transfer) {
//        System.out.println("transfer:" + transfer);
//        transferService.transferMoney(transfer);
//        Map<String, Integer> map = new HashMap<>();
//        map.put("status", 200);
//        return map;
//    }
//
//    @RequestMapping("/transferMoneyOver")
//    @ResponseBody
//    public Map<String, Integer> transferMoneyOver(Transfer transfer) {
//        Map<String, Integer> map = new HashMap<>();
//        System.out.println("正在进行跨境转账。。。");
//        transferService.transferMoneyOver(transfer);
//        map.put("status", 200);
//        return map;
//    }
//
//    /**
//     * 查询余额
//     *
//     * @param accNo 卡号
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/queryBalance")
//    public Map<String, BigDecimal> queryBalance(@RequestParam("accNo") String accNo) {
//        Map<String, BigDecimal> map = new HashMap<>();
//        BigDecimal balance = transferService.queryBalance(accNo);
//        map.put("balance", balance);
//        System.out.println(balance);
//        return map;
//    }
//
//    /**
//     * 根据卡号查询用户名和银行
//     * 做测试用
//     *
//     * @param accNo 卡号
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/queryBankAndUserName")
//    public Map<String, String> queryBankAndUserName(@RequestParam("accNo") String accNo) {
//        Map<String, String> map = transferService.queryBankAndUserName(accNo);
//        return map;
//    }
//
//    /**
//     * 根据流水号查账单信息
//     *
//     * @param dealNo 流水号
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/queryTransferByDealNo")
//    public Transfer queryTransferByDealNo(@RequestParam("dealNo") String dealNo) {
//        System.out.println(dealNo);
//        Transfer transfer = transferService.queryTransferByDealNo(dealNo);
//        System.out.println(transfer);
//        return transfer;
//    }
//
//    /**
//     * 跳转到转账页面，做测试
//     *
//     * @return
//     */
//    @RequestMapping("/toTransfer")
//    public ModelAndView toTransfer() {
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("transferAccounts");
//        return mv;
//    }

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
        Map<String, Boolean> map = new HashMap<>();
        System.out.println("userName:"+userName+" accNo:"+accNo);
        Boolean flag = transferService.checkUser(userName, accNo);
        //存在返回true,不存在返回false
        System.out.println("执行结果：" + flag);
        map.put("status", flag);
        return map;
    }

//    /**
//     * 填充用户名和账户
//     *
//     * @param accInName
//     * @param accIn
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/addUser")
//    public ModelAndView addUser(@RequestParam("accInName") String accInName, @RequestParam("accIn") String accIn,
//                                @RequestParam("accInBank") String accInBank) {
//        ModelAndView mv = new ModelAndView();
//        Map<String, String> map = new HashMap<>();
//        map.put("accInName", accInName);
//        map.put("accIn", accIn);
//        map.put("accInBank", accInBank);
//        System.out.println(accInBank);
//        mv.addObject("map", map);
//        mv.setViewName("transferAccounts");
//        return mv;
//    }

}
