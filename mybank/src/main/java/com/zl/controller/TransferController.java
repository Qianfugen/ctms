package com.zl.controller;

import com.zl.api.CheckUser2API;
import com.zl.api.CheckUserAPI;
import com.zl.api.JobAPI;
import com.zl.pojo.Transfer;
import com.zl.pojo.UsualColl;
import com.zl.service.TransferService;
import org.mybatis.spring.annotation.MapperScan;
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
import java.util.List;
import java.util.Map;

/**
 * @author root
 */
@Controller
@RequestMapping("/transfer")
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private JobAPI jobAPI;
    @Autowired
    private CheckUser2API checkUser2API;
    @Autowired
    private CheckUserAPI checkUserAPI;

    /**
     * 分类转账
     * status: 0 冻结，1 定时任务取消成功， 100 超出上限，200 转账成，400 余额不足
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/sortTransfer")
    public ModelAndView sortTransfer(Transfer transfer, HttpSession session, @RequestParam("bank") String bank) {
        ModelAndView mv = new ModelAndView();
        //结果页面
        mv.setViewName("transferAccountResult");
        //预先设置一个转出账户，本应从页面获取，这里仅做测试
        String accOut= (String) session.getAttribute("loginAccNo");
        System.out.println("当前用户卡号accNO:"+accOut);
        System.out.println("transfer"+transfer);
        transfer.setAccOut(accOut);
        //启动账户
//        transfer.setAccOut("6222303626811324642");
        //冻结账户
//        transfer.setAccOut("6222304497903198673");
        Map<String, Integer> map = transferService.verifyTransfer(transfer, bank);
        mv.addObject("map", map);
        return mv;

    }

    /**
     * 同行转账
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/transferMoney")
    public Map<String, Integer> transferMoney(@RequestBody Transfer transfer) {
        System.out.println("transfer:" + transfer);
        transferService.transferMoney(transfer);
        Map<String, Integer> map = new HashMap<>();
        map.put("status", 200);
        return map;
    }

    /**
     * 跨行转账
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/transferMoneyDemo")
    public Map<String, Integer> transferMoneyDemo(@RequestBody Transfer transfer) {
        System.out.println("transfer:" + transfer);
        System.out.println("正在进行跨行转账。。。");
        transferService.transferMoneyDome(transfer);
        Map<String, Integer> map = new HashMap<>();
        map.put("status", 200);
        return map;
    }
    /**
     * 跨境转账
     *
     * @param transfer
     * @return
     */
    @RequestMapping("/transferMoneyOver")
    @ResponseBody
    public Map<String, Integer> transferMoneyOver(Transfer transfer) {
        Map<String, Integer> map = new HashMap<>();
        System.out.println("正在进行跨境转账。。。");
        transferService.transferMoneyOver(transfer);
        map.put("status", 200);
        return map;
    }

    /**
     * 删除定时转账，从session获取用户卡号
     * status: 0 冻结，1 定时任务取消成功， 100 超出上限，200 转账成，400 余额不足
     *
     * @param accNo
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteJob")
    public ModelAndView deleteJob(@RequestParam("accNo") String accNo) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("transferAccountResult");
        String result = jobAPI.deleteJob(accNo);
        Map<String, Integer> map = new HashMap<>();
        if (result.equals("success")) {
            map.put("status", 1);
        }
        mv.addObject("map", map);
        return mv;
    }

    /**
     * 查询余额
     *
     * @param accNo 卡号
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryBalance")
    public Map<String, BigDecimal> queryBalance(@RequestParam("accNo") String accNo) {
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal balance = transferService.queryBalance(accNo);
        map.put("balance", balance);
        System.out.println(balance);
        return map;
    }

    /**
     * 根据卡号查询用户名和银行
     * 做测试用
     *
     * @param accNo 卡号
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryBankAndUserName")
    public Map<String, String> queryBankAndUserName(@RequestParam("accNo") String accNo) {
        Map<String, String> map = transferService.queryBankAndUserName(accNo);
        return map;
    }

    /**
     * 根据流水号查账单信息
     *
     * @param dealNo 流水号
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryTransferByDealNo")
    public Transfer queryTransferByDealNo(@RequestParam("dealNo") String dealNo) {
        System.out.println(dealNo);
        Transfer transfer = transferService.queryTransferByDealNo(dealNo);
        System.out.println(transfer);
        return transfer;
    }

    /**
     * 跳转到转账页面，做测试
     *
     * @return
     */
    @RequestMapping("/toTransfer")
    public ModelAndView toTransfer(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String accNo= (String) session.getAttribute("loginAccNo");
        List<UsualColl> usualColls = transferService.queryCusUsual(accNo);
        System.out.println(usualColls);
        mv.addObject("usualColls",usualColls);
        mv.setViewName("transferAccounts");
        return mv;
    }

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
        Boolean flag=false;
        if(accNo.matches("^622230.*")){
            System.out.println("本行账户");
             flag= transferService.checkUser(userName, accNo);
        }else {
            System.out.println("他行账户");
            flag=checkUser2API.checkUser(userName,accNo).get("status");
        }
        Map<String, Boolean> map = new HashMap<>();


        //存在返回true,不存在返回false
        System.out.println("执行结果：" + flag);
        map.put("status", flag);
        return map;
    }

    /**
     * 根据卡号和用户名验证用户是否存在
     *
     * @param userName
     * @param accNo
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUser2")
    public Map<String, Boolean> checkUser2(@RequestParam("userName") String userName, @RequestParam("accNo") String accNo) {
        Map<String, Boolean> map = new HashMap<>();
        System.out.println("checkUser2");
        Boolean flag = checkUserAPI.checkUser(userName, accNo).get("status");
        //存在返回true,不存在返回false
        System.out.println("执行结果：" + flag);
        map.put("status", flag);
        return map;
    }

    /**
     * 填充用户名和账户
     *
     * @param accInName
     * @param accIn
     * @return
     */
    @ResponseBody
    @RequestMapping("/addUser")
    public ModelAndView addUser(@RequestParam("accInName") String accInName, @RequestParam("accIn") String accIn,HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String accNo= (String) session.getAttribute("loginAccNo");
        List<UsualColl> usualColls = transferService.queryCusUsual(accNo);
        Map<String, String> map = new HashMap<>();
        map.put("accInName", accInName);
        map.put("accIn", accIn);
        mv.addObject("usualColls",usualColls);
        mv.addObject("map", map);
        mv.setViewName("transferAccounts");
        return mv;
    }

}
