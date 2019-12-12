package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * 客户信息
 *
 * @author root
 */

public class User implements Serializable {
    /**
     * 客户号
     */
    private String userId;
    /**
     * 客户姓名
     */
    private String userName;
    /**
     * 性别 0 不详  1 男  2 女
     */
    private Integer userSex;
    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date userBirthdate;
    /**
     * 证件号码
     */
    private String userCertNo;
    /**
     * 住址
     */
    private String userAddr;
    /**
     * 邮政编码
     */
    private String userZipCode;
    /**
     * 固定电话
     */
    private String userTelNo;
    /**
     * 手机号码
     */
    private String userPhoneNo;
    /**
     * 邮箱
     */
    private String userEmail;
    /**
     * 登录密码
     */
    private String userPwd;
    /**
     * 用户卡
     */
    private Account account;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public Date getUserBirthdate() {
        return userBirthdate;
    }

    public void setUserBirthdate(Date userBirthdate) {
        this.userBirthdate = userBirthdate;
    }

    public String getUserCertNo() {
        return userCertNo;
    }

    public void setUserCertNo(String userCertNo) {
        this.userCertNo = userCertNo;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getUserZipCode() {
        return userZipCode;
    }

    public void setUserZipCode(String userZipCode) {
        this.userZipCode = userZipCode;
    }

    public String getUserTelNo() {
        return userTelNo;
    }

    public void setUserTelNo(String userTelNo) {
        this.userTelNo = userTelNo;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userSex=" + userSex +
                ", userBirthdate=" + userBirthdate +
                ", userCertNo='" + userCertNo + '\'' +
                ", userAddr='" + userAddr + '\'' +
                ", userZipCode='" + userZipCode + '\'' +
                ", userTelNo='" + userTelNo + '\'' +
                ", userPhoneNo='" + userPhoneNo + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", account=" + account +
                '}';
    }
}
