package com.zl.controller;

import com.zl.pojo.*;
import com.zl.service.TransferService;
import com.zl.service.UserService;
import com.zl.service.UsualCollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/UsualColl")
public class UsualCollController {
    @Autowired
    private UsualCollService ucs;

    @Autowired
    private TransferService ts;

    @Autowired
    private UserService us;

    @RequestMapping("/deleteUsuslColl")
    public ModelAndView deleteUsuslColl(@RequestParam("accIn") String accIn){
        ModelAndView mv=new ModelAndView();
        int flag=ucs.deleteUsualColl(accIn);
        if(flag>0){
            mv.setViewName("queryUsualCollByFy");
        }else {
            System.out.println("错误");
        }
        return mv;
    }


    @RequestMapping("/queryUsualCollByFy")
    public ModelAndView queryUsualCollByFy(UFenYe uFenYe, HttpSession session ){
        ModelAndView mv=new ModelAndView();
        String loginAccNo= (String) session.getAttribute("loginAccNo");
        if (uFenYe.getUquery()!=null){
            uFenYe.getUquery().setqMainAcc(loginAccNo);
        }else{
            Uquery uquery=new Uquery();
            uquery.setqMainAcc(loginAccNo);
            uFenYe.setUquery(uquery);
        }
        List<UsualColl> usualColls = ucs.queryUsualCollByFy(uFenYe);
        mv.addObject("usualColls",usualColls);
        int flag = 0;
        for (UsualColl u :
                usualColls) {
            System.out.println(u);
            flag++;

        }
        System.out.println(flag);
        mv.addObject("uFenYe",uFenYe);
        mv.setViewName("payeeManagement");
        System.out.println(uFenYe+"lllll");
        return mv;
    }


    @RequestMapping("toAdd")
    public String toAdd(){
        return "payeeManagement01";
    }

    @RequestMapping("/addUsualColl")
    public ModelAndView addUsualColl(@RequestParam("accIn")String accIn, @RequestParam("accInName")String accInName, @RequestParam("transFund") BigDecimal transFund, HttpSession ss){
        ModelAndView mv=new ModelAndView();
        UsualColl uc=new UsualColl();
        String loginAccNo= (String) ss.getAttribute("loginAccNo");
        System.out.println(loginAccNo+"***********************************");
        uc.setAccIn(accIn);
        uc.setMainAcc(loginAccNo);
        uc.setAccInName(accInName);
        uc.setTransFund(transFund);
        int flag=ucs.addUsualColl(uc);
        mv.setViewName("queryUsualCollByFy");
        return mv;
    }

    @RequestMapping("/batchTransfer")
    public ModelAndView batchTransfer(String[] fkcheck,String[] accIn,BigDecimal[] transFund,HttpSession session){
        ModelAndView mv = new ModelAndView();
        List<Transfer> transferList=new ArrayList<Transfer>();
        String loginAccNo = (String) session.getAttribute("loginAccNo");
        System.out.println(fkcheck.length+">>"+accIn.length+"hh"+transFund.length);

        for (int i = 0; i <fkcheck.length; i++) {
            for (int j = 0; j < accIn.length; j++) {
                if (fkcheck[i].equals(accIn[j])){
                    Transfer transfer = new Transfer();
                    transfer.setAccIn(accIn[j]);
                    transfer.setAccOut(loginAccNo);
                    transfer.setTransFund(transFund[j]);
                    transferList.add(transfer);
                }
            }
        }
        for (Transfer t :transferList) {
            ts.transferMoney(t);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("status", 200);
        mv.addObject("map",map);
        mv.setViewName("transferAccountResult");
        return mv;
    }

    /**
     * 验证卡号是否和姓名一致
     * @param accInName
     * @param accIn
     * @return
     */
    @RequestMapping("/regName")
    @ResponseBody
    public Map<String,Object> regName(String accInName,String accIn){
        Map<String,Object> map = new HashMap<>();
        User user = us.queryCustom(accIn);
        if(user.getUserName().equals(accInName)){
            map.put("flag",true);
        }else{
            map.put("flag",false);
        }
        return map;
    }
}