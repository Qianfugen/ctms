package com.zl.pojo;

/**
 * 银行卡账号表
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author root
 */
public class Account implements Serializable {
    /**
     * 账号
     */
    private String accNo;
    /**
     * 货币代码
     */
    private String currType;
    /**
     * 银行代码
     */
    private String accBankId;
    /**
     * 客户号
     */
    private String userId;
    /**
     * 账号状态
     */
    private Integer accStatus;
    /**
     * 交易限额
     */
    private BigDecimal accLimit;
    /**
     * 账号资金
     */
    private BigDecimal accFund;
    /**
     * 账号类型
     */
    private String accType;
    /**
     * 开户时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accDate;
    /**
     * 支付密码
     */
    private String accPwd;

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getCurrType() {
        return currType;
    }

    public void setCurrType(String currType) {
        this.currType = currType;
    }

    public String getAccBankId() {
        return accBankId;
    }

    public void setAccBankId(String accBankId) {
        this.accBankId = accBankId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(Integer accStatus) {
        this.accStatus = accStatus;
    }

    public BigDecimal getAccLimit() {
        return accLimit;
    }

    public void setAccLimit(BigDecimal accLimit) {
        this.accLimit = accLimit;
    }

    public BigDecimal getAccFund() {
        return accFund;
    }

    public void setAccFund(BigDecimal accFund) {
        this.accFund = accFund;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public Date getAccDate() {
        return accDate;
    }

    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    public String getAccPwd() {
        return accPwd;
    }

    public void setAccPwd(String accPwd) {
        this.accPwd = accPwd;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accNo='" + accNo + '\'' +
                ", currType='" + currType + '\'' +
                ", accBankId='" + accBankId + '\'' +
                ", userId='" + userId + '\'' +
                ", accStatus=" + accStatus +
                ", accLimit=" + accLimit +
                ", accFund=" + accFund +
                ", accType='" + accType + '\'' +
                ", accDate=" + accDate +
                ", accPwd='" + accPwd + '\'' +
                '}';
    }
}
