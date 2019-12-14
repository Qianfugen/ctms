package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author junqi
 * 登入记录表
 */
public class Login implements Serializable {
    /**
     * 账号
     */
    private String accNO;
    /**
     * 登入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date loginTime;
    /**
     * 登入地址
     */
    private String loginAdd;
    /**
     * 登入ip
     */
    private String loginIp;
    /**
     * 登入状态
     */
    private String loginStatus;
    /**
     * 当天登入次数
     */
    private Integer loginCount;
    /**
     * 连续登入失败次数
     */
    private Integer loginError;
    /**
     *登入异常原因
     */
    private String loginOdd;

    /**
     * 登入主机名
     */
    private String hostName;

    @Override
    public String toString() {
        return "Login{" +
                "accNO='" + accNO + '\'' +
                ", loginTime=" + loginTime +
                ", loginAdd='" + loginAdd + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginStatus='" + loginStatus + '\'' +
                ", loginCount=" + loginCount +
                ", loginError=" + loginError +
                ", loginOdd='" + loginOdd + '\'' +
                ", hostName='" + hostName + '\'' +
                '}';
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getAccNO() {
        return accNO;
    }

    public void setAccNO(String accNO) {
        this.accNO = accNO;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginAdd() {
        return loginAdd;
    }

    public void setLoginAdd(String loginAdd) {
        this.loginAdd = loginAdd;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getLoginError() {
        return loginError;
    }

    public void setLoginError(Integer loginError) {
        this.loginError = loginError;
    }

    public String getLoginOdd() {
        return loginOdd;
    }

    public void setLoginOdd(String loginOdd) {
        this.loginOdd = loginOdd;
    }
}
