package com.zl.controller;

import com.netflix.ribbon.proxy.annotation.Http;
import com.zl.pojo.FenYe;
import com.zl.pojo.Query;
import com.zl.pojo.Transfer;
import com.zl.pojo.User;
import com.zl.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 登录注册控制层
 *
 * @author 徐浩杰
 * @version 1.0 2019-12-12
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService us;


    /**
     * 注册(卡号)
     *
     * @return
     */
    @RequestMapping("/register")
    public ModelAndView register(String accNo, String password) {
        System.out.println("进入注册控制层");
        System.out.println("accNo： " + accNo);
        System.out.println("password： " + password);
        ModelAndView mv = new ModelAndView();
        if (accNo != null && !accNo.equals("") && password != null && !password.equals("")) {
            us.register(accNo, password);
            mv.setViewName("toLogin");
        } else {
            mv.addObject("error", "请填写注册信息");
            mv.setViewName("toRegister");
        }
        return mv;
    }

    /**
     * ajax后台检测是否重复注册
     *
     * @return false 重复 true 合法
     */
    @ResponseBody
    @RequestMapping("/regName")
    public Map<String, Object> regName(String accNo) {
        System.out.println("regName");
        return us.regName(accNo);
    }

    /**
     * 提供加密密码的接口
     */
    @ResponseBody
    @RequestMapping("/regUserPwd")
    @ResponseBody
    public String regUserPwd(@RequestBody User user) {
        System.out.println(user + "---------------------");
        return us.regUserPwd(user.getAccount().getAccNo(), user.getUserPwd());
    }

    /**
     * 登录
     *
     * @return
     */
    @RequestMapping("/login")
    public ModelAndView login(HttpSession session, String accNo, String password) {
        ModelAndView mv = new ModelAndView();
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(accNo, password);
        System.out.println("登录控制层:" + accNo + "...." + password);

        try {
            //执行认证
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            System.out.println("ice异常......");
            mv.addObject("error", "用户名或密码错误");
            mv.setViewName("login");
            return mv;
        } catch (UnknownAccountException uae) {
            System.out.println("uae异常......");
            mv.addObject("error", "登录出错");
            mv.setViewName("login");
            return mv;
        } catch (Exception e) {
            System.out.println("e异常......");
            mv.addObject("error", "登录出错啦....");
            mv.setViewName("login");
            return mv;
        }
        System.out.println("验证通过");
        System.out.println("通过" + accNo);
        mv.addObject("accNo", accNo);
        mv.setViewName("toIndex");
        return mv;
    }

    /**
     * 退出
     *
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout() {
        ModelAndView mv = new ModelAndView();
        us.logout();
        mv.setViewName("toLogin");
        return mv;
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/toRegister")
    public String toRegister() {
        return "register";
    }

    @RequestMapping("/toIndex")
    public ModelAndView toIndex(HttpSession session, String accNo) {
        ModelAndView mv = new ModelAndView();
        System.out.println("进入index");
        User loginUser = us.queryCustom(accNo);
        System.out.println("当前对象:" + loginUser);
        session.setAttribute("loginUser", loginUser);
        session.setAttribute("loginAccNo", accNo);
        mv.setViewName("index");
        return mv;
    }

    @RequestMapping("/notLogin")
    public String notLogin() {
        return "notLogin";
    }

    @RequestMapping("/queryCustom")
    public ModelAndView queryCustom(@RequestParam("accNo") String accNO) {
        System.out.println("accNo:" + accNO);
        ModelAndView mv = new ModelAndView();
        User user = us.queryCustom(accNO);
        mv.addObject("user", user);
        mv.setViewName("CustermInfo");
        return mv;
    }

    @RequestMapping("/toCustomRecord")
    public ModelAndView toCustomRecord(FenYe fenYe, HttpSession session) {
        String accNo = (String) session.getAttribute("loginAccNo");
        System.out.println("accNo: " + accNo);
        Query query = new Query();
        if (fenYe.getQuery() != null) {
            query = fenYe.getQuery();
        }
        query.setqAccNo(accNo);
        fenYe.setQuery(query);
        ModelAndView mv = new ModelAndView();
        List<Transfer> transfers = us.queryTransferByAccNo(fenYe);
        if (transfers != null) {
            mv.addObject("transfers", transfers);
        } else {
            mv.addObject("message", "该用户暂无交易记录");
        }
        mv.setViewName("transferInfo");
        return mv;
    }

}
