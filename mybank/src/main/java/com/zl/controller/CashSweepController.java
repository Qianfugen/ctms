package com.zl.controller;

import com.zl.pojo.Account;
import com.zl.pojo.Coll;
import com.zl.pojo.Transfer;
import com.zl.service.CashSweepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * @author fm
 * @version 1.0
 * @date 2019/12/10 17:35
 */
@Controller
@RequestMapping("/cashSweep")
public class CashSweepController {
    @Autowired
    private CashSweepService cashSweepService;

    @RequestMapping("/queryMainAccCollStatus")
    @ResponseBody
    public Map<String,String> queryMainAccCollStatus(@RequestParam("mainAcc") String mainAcc) {
        Map<String,String> status=new HashMap<>();
        String collStatus = cashSweepService.queryCollStatus(mainAcc);
        status.put("collStatus",collStatus);
        return status;
    }

    @RequestMapping("/loginAccountCollStatus")
    @ResponseBody
    public ModelAndView loginAccountCollStatus(HttpSession session) {
        ModelAndView mv = new ModelAndView();

        //避免魔法值，定义账号的资金归集签约状态
        String collStatus1 = "未签约";
        String collStatus2 = "已签约";
        String collStatus3 = "主账号";

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");
        //获取当前登录的账号的资金归集签约状态
        String collStatus = loginAccount.getCollStatus();

        //如果签约状态为"未签约"或者null
        if (collStatus == null || "".equals(collStatus) || collStatus1.equals(collStatus)) {
            mv.addObject("message","您还没有签约资金归集，请先签约！");
            mv.setViewName("####到签约页面####");
        }

        //如果签约状态为"已签约"
        if (collStatus2.equals(collStatus)) {
            mv.setViewName("queryColl");
        }

        //如果签约状态为"主账号"
        if (collStatus3.equals(collStatus)) {
            mv.setViewName("queryAllColl");
        }
        return mv;
    }

    @RequestMapping("/signColl")
    @ResponseBody
    public ModelAndView signColl(HttpSession session, Coll coll, String signFund) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");
        //获取需要签约的主账号的签约状态
        String collStatus = cashSweepService.queryCollStatus(coll.getMainAcc());

        //测试代码
        System.out.println(collStatus);

        //调用签约方法
        int flag = cashSweepService.signColl(loginAccount, coll, collStatus, signFund);

        //如果签约失败，跳转到签约页面；否则跳转到签约信息页面
        if (flag == 0) {
            mv.addObject("error", "签约失败。。。。");
            mv.setViewName("####到签约页面####");
        } else {
            //将会话中登录的账号签约信息更新
            loginAccount.setCollStatus(cashSweepService.queryCollStatus(loginAccount.getAccNo()));
            session.setAttribute("loginAccount", loginAccount);
            mv.setViewName("loginAccountCollStatus");
        }
        return mv;
    }

    @RequestMapping("/cancelColl")
    @ResponseBody
    public ModelAndView cancelColl(HttpSession session, String followAcc, String mainAcc) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        //解除签约是否成功的标志
        int flag = 0;
        //判断是在子账号端进行的取消还是主账户端进行的取消
        //如果没有传入子账号的字符串，说明是子账户端的取消
        if (followAcc == null || "".equals(followAcc)) {
            //由传入的主账号mainAcc创建一个用于取消签约的主账号
            Account mainAccount = new Account();
            mainAccount.setAccNo(mainAcc);
            //调用解约的方法
            flag = cashSweepService.cancelColl(loginAccount, mainAccount);

            //如果解约失败，跳转到签约信息页面
            if (flag == 0) {
                mv.addObject("error", "解约失败");
                mv.setViewName("redirect:queryColl");
            }
        }

        //如果没有传入主账号的字符串，说明是主账户端的取消
        if (mainAcc == null || "".equals(mainAcc)) {
            //由传入的子账号followAcc创建一个用于取消签约的子账号
            Account viceAccount = new Account();
            viceAccount.setAccNo(followAcc);
            //调用解约的方法
            flag = cashSweepService.cancelColl(viceAccount, loginAccount);

            //如果解约失败，跳转到签约信息页面
            if (flag == 0) {
                mv.addObject("error", "解约失败");
                mv.setViewName("redirect:queryAllColl");
            }
        }

        //解约成功，更新会话中的账号对象的签约状态；跳转到签约状态判断
        loginAccount.setCollStatus(cashSweepService.queryCollStatus(loginAccount.getAccNo()));
        session.setAttribute("loginAccount", loginAccount);
        mv.setViewName("redirect:loginAccountCollStatus");

        return mv;
    }

    @RequestMapping("/updateColl")
    @ResponseBody
    public ModelAndView updateColl(Coll reviseColl, String signFund, HttpSession session) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        //获取需要修改签约的主账号的签约状态,并调用修改签约的方法
        String collStatus = cashSweepService.queryCollStatus(reviseColl.getMainAcc());
        int flag = cashSweepService.updateColl(reviseColl, loginAccount, signFund, collStatus);

        //如果修改签约信息失败；修改成功重新跳转到签约状态判断
        if (flag == 0) {
            mv.addObject("error", "修改签约信息失败");
            mv.setViewName("redirect:queryColl");
        } else {
            mv.setViewName("redirect:loginAccountCollStatus");
        }
        return mv;
    }

    @RequestMapping("/queryColl")
    @ResponseBody
    public ModelAndView queryColl(HttpSession session) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        //查询当前账号（子账号）的归集签约信息
        Coll coll = cashSweepService.queryColl(loginAccount);

        mv.addObject("coll", coll);
        mv.setViewName("####转到已签约页面#####");
        return mv;
    }

    @RequestMapping("/queryAllColl")
    @ResponseBody
    public ModelAndView queryAllColl(HttpSession session) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        //查询当前账号（主账号）的归集签约信息
        List<Coll> colls = cashSweepService.queryMainColl(loginAccount);

        mv.addObject("colls", colls);
        mv.setViewName("####转到主账号页面#####");
        return mv;
    }

    @RequestMapping("/queryTransfers")
    @ResponseBody
    public ModelAndView queryTransfers(HttpSession session) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        //查询当前登录账号的归集记录
        List<Transfer> transfers = cashSweepService.queryTransfers(loginAccount);

        //如果没有查询到相关内容，显示“没有相关归集记录”；否则显示相关信息
        if(transfers==null){
            mv.addObject("message","没有相关归集记录！");
        }else {
            mv.addObject("transfers", transfers);
        }
        mv.setViewName("transfers");
        return mv;
    }

    @RequestMapping("/test")
    public ModelAndView test(HttpSession session){
        ModelAndView mv=new ModelAndView();
        System.out.println("连接服务成功");
        Account loginAccount=new Account();
//        loginAccount.setCollStatus("主账号");
        loginAccount.setAccNo("6222308875601202830");
        session.setAttribute("loginAccount",loginAccount);
        mv.setViewName("signColl");
        return mv;
    }
}
