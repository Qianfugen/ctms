package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author junqi
 * 催款通知
 */
@Component
public class PayInfo implements Serializable {
    /**
     * 贷方账号
     */
    private String creditorAcc;
    /**
     * 贷方用户名
     */
    private String creditorName;
    /**
     * 支付金额
     */
    private BigDecimal fund;
    /**
     * 催款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date infoTime;
    /**
     *借方账户
     */
    private String debtor;
    /**
     * 借方用户名
     */
    private String debtorName;

    public String getCreditorAcc() {
        return creditorAcc;
    }

    public void setCreditorAcc(String creditorAcc) {
        this.creditorAcc = creditorAcc;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public BigDecimal getFund() {
        return fund;
    }

    public void setFund(BigDecimal fund) {
        this.fund = fund;
    }

    public Date getInfoTime() {
        return infoTime;
    }

    public void setInfoTime(Date infoTime) {
        this.infoTime = infoTime;
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    @Override
    public String toString() {
        return "PayInfo{" +
                "creditorAcc='" + creditorAcc + '\'' +
                ", creditorName='" + creditorName + '\'' +
                ", fund=" + fund +
                ", infoTime=" + infoTime +
                ", debtor='" + debtor + '\'' +
                ", debtorName='" + debtorName + '\'' +
                '}';
    }
}
