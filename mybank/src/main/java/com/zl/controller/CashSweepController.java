package com.zl.controller;

import com.zl.pojo.*;
import com.zl.service.CashSweepService;
import com.zl.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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
    @Autowired
    private TransferService transferService;

    @RequestMapping("/queryMainAccCollStatus")
    @ResponseBody
    public Map<String,Object> queryMainAccCollStatus(@RequestParam("mainAcc") String mainAcc,@RequestParam("username") String username) {
        Map<String, Object> result=new HashMap<>();
        //查询账号
        Account account = cashSweepService.queryAccount(mainAcc);
        //如果账号不存在
        if(account==null){
            result.put("flag",false);
            result.put("error","账号不存在");
            //直接返回
            return result;
        }
        // 账号存在，判断账号的签约状态
        String collStatus=account.getCollStatus();
        //如果账号签约状态为已签约，校验失败
        if("已签约".equals(collStatus)){
            //不可签约
            result.put("flag",false);
            result.put("error","该账号不可签约");
            return result;
        }

        //如果可以签约，校验用户名
        Map<String, String> message = transferService.queryBankAndUserName(mainAcc);
        if(username.equals(message.get("userName"))){
            //名字通过校验,返回账号的开户行
            result.put("flag",true);
            result.put("bankName",message.get("bankName"));
        }else {
            //用户名与卡号不对应
            result.put("flag",false);
            result.put("error","用户名与卡号不对应");
            return result;
        }
        return result;
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
            mv.setViewName("fundCollectionl01");
        }

        //如果签约状态为"已签约"
        if (collStatus2.equals(collStatus)) {
            mv.setViewName("queryColl");
        }

        //如果签约状态为"主账号"
        if (collStatus3.equals(collStatus)) {
            mv.setViewName("queryMainCollByFenYe");
        }
        return mv;
    }

    @RequestMapping("/signColl")
    @ResponseBody
    public ModelAndView signColl(HttpSession session, Coll coll) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        //调用签约方法
        int flag = cashSweepService.signColl(loginAccount, coll);

        //如果签约失败，跳转到签约页面；否则跳转到签约信息页面
        if (flag == 0) {
            mv.addObject("error", "签约失败。。。。");
            mv.setViewName("fundCollectionl01");
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
                mv.setViewName("queryColl");
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
                mv.setViewName("queryMainCollByFenYe");
            }
        }

        //解约成功，更新会话中的账号对象的签约状态；跳转到签约状态判断
        loginAccount.setCollStatus(cashSweepService.queryCollStatus(loginAccount.getAccNo()));
        session.setAttribute("loginAccount", loginAccount);
        mv.setViewName("loginAccountCollStatus");

        return mv;
    }

    @RequestMapping("/beforeUpdate")
    @ResponseBody
    public ModelAndView beforeUpdate(Coll coll){
        ModelAndView mv=new ModelAndView();
        mv.addObject("coll",coll);
        mv.setViewName("fundCollectionl03");
        return mv;
    }

    @RequestMapping("/updateColl")
    @ResponseBody
    public ModelAndView updateColl(Coll reviseColl, HttpSession session) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        //调用修改签约的方法
        int flag = cashSweepService.updateColl(reviseColl, loginAccount);

        //如果修改签约信息失败；修改成功重新跳转到签约状态判断
        if (flag == 0) {
            mv.addObject("error", "修改签约信息失败");
            mv.setViewName("queryColl");
        } else {
            mv.setViewName("loginAccountCollStatus");
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
        Coll coll = cashSweepService.queryColl(loginAccount.getAccNo());

        mv.addObject("coll", coll);
        mv.setViewName("fundCollectionl02");
        return mv;
    }

    @RequestMapping("/queryMainCollByFenYe")
    @ResponseBody
    public ModelAndView queryMainCollByFenYe(HttpSession session,FenYe fenYe) {
        ModelAndView mv = new ModelAndView();

        //获取当前登录的账号
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        if(fenYe.getQuery()==null){
            Query query=new Query();
            query.setqMainAccNo(loginAccount.getAccNo());
            fenYe.setQuery(query);
        }else {
            fenYe.getQuery().setqMainAccNo(loginAccount.getAccNo());
        }
        //查询当前账号（主账号）的归集签约信息
        List<Coll> colls = cashSweepService.queryMainCollByFenYe(fenYe);

        mv.addObject("colls", colls);
        mv.addObject("fenYe",fenYe);
        mv.setViewName("fundCollectionl04");
        return mv;
    }

    @RequestMapping("/queryTransfersByFenYe")
    @ResponseBody
    public ModelAndView queryTransfersByFenYe(FenYe fenYe) {
        ModelAndView mv = new ModelAndView();

        //查询当前登录账号的归集记录
        List<Transfer> trans = cashSweepService.queryTransfersByFenYe(fenYe);

        //如果没有查询到相关内容，显示“没有相关归集记录”；否则显示相关信息
        if(trans==null||trans.size()==0){
            mv.addObject("message","没有相关归集记录！");
        }else {
            mv.addObject("fenYe",fenYe);
            mv.addObject("trans", trans);
        }
        mv.setViewName("fundCollectionl05");
        return mv;
    }

    @RequestMapping("/test")
    public ModelAndView test(HttpSession session){
        ModelAndView mv=new ModelAndView();
        System.out.println("连接服务成功");
        Account loginAccount=cashSweepService.queryAccount("6222302919195600644");
        System.out.println(transferService.queryBankAndUserName("6222307120415995107"));

        session.setAttribute("loginAccount",loginAccount);
        mv.setViewName("loginAccountCollStatus");
        return mv;
    }
}
