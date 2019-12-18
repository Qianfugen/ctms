package com.zl.controller;

import com.zl.pojo.Uquery;
import com.zl.pojo.UsualColl;
import com.zl.pojo.uFenYe;
import com.zl.service.UsualCollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/UsualColl")
public class UsualCollController {
    @Autowired
    private UsualCollService ucs;
    @RequestMapping("/deleteUsuslColl")
    public ModelAndView deleteUsuslColl(@RequestParam("accIn") String accIn){
        ModelAndView mv=new ModelAndView();
        int flag=ucs.deleteUsualColl(accIn);
        if(flag>0){
            mv.setViewName("/UsualColl/toPayeeManagement");
        }else {
            System.out.println("错误");
        }
        return mv;
    }
    @RequestMapping("/addUsualColl")
   public ModelAndView addUsualColl(@RequestParam("accIn")String accIn, @RequestParam("accInName")String accInName, @RequestParam("transFund") BigDecimal transFund, HttpSession ss){
       ModelAndView mv=new ModelAndView();
        UsualColl uc=new UsualColl();
        String accNo= (String) ss.getAttribute("loginAccNo");
        System.out.println(accNo+"***********************************");
        uc.setAccIn(accIn);
        uc.setMainAcc("6222309287143333829");
        uc.setAccInName(accInName);
        uc.setTransFund(transFund);
        int num=ucs.queryUserByAccNo(uc);
       if(num>0){

           System.out.println(uc);
           int flag=ucs.addUsualColl(uc);
            if(flag>0){
                mv.setViewName("queryAllUsualColl");
            }else {
                mv.setViewName("toAddUser");
            }
        }else {
            mv.setViewName("toAddUser");
        }
        return mv;
   }
    @RequestMapping("/queryAllUsualColl")
    public ModelAndView queryAllUsualColl(HttpSession session){
        ModelAndView mv=new ModelAndView();
        session.getAttribute("loginAccNo");
        List<UsualColl> usualColls=ucs.queryAllUsualColl("6222304960031779591");
        mv.addObject("usualColles",usualColls);
        mv.setViewName("/UsualColl/toPayeeManagement");
        return mv;
    }
    @RequestMapping("/queryUsualColl")
    @ResponseBody
    public String queryUsualColl(@RequestParam("accIn")String accIn){
        String mess=null;
        UsualColl usualColl=new UsualColl();
        usualColl=ucs.queryUsualColl(accIn);
        if(usualColl==null){
            mess="{\"flag\":false}";
        }else {
            mess="{\"flag\":true}";
        }
        return mess;
    }
    @RequestMapping("/toPayeeManagement")
    public ModelAndView toPayeeManagement(HttpSession session){
        ModelAndView mv=new ModelAndView();
        String accNo= (String) session.getAttribute("loginAccNo");

        List<UsualColl> usualColls=ucs.queryAllUsualColl("6222309287143333829");
        mv.addObject("usualColls",usualColls);
        mv.setViewName("payeeManagement");
        return mv;
    }
    @RequestMapping("/toPay")
    public String toPay(){
        return "payeeManagement01";
    }
    @RequestMapping("/queryUsualCollByFy")
    public ModelAndView queryUsualCollByFy(@RequestParam("accIn")String accIn,@RequestParam("accInName")String accInName ){
        ModelAndView mv=new ModelAndView();
        uFenYe u=new uFenYe();
        Uquery uquery=new Uquery();
        uquery.setqAccIn(accIn);
        uquery.setqAccInName(accInName);
        u.setUquery(uquery);
        List<UsualColl>usualColls=ucs.queryUsualCollByFy(u);
        mv.addObject("usualColls",usualColls);
        mv.setViewName("payeeManagement");
        return mv;

    }

    @RequestMapping("/toAddUser")
    public ModelAndView toAddUser(){
        ModelAndView mv=new ModelAndView();

        mv.setViewName("payeeManagement01");
        return mv;

    }
}