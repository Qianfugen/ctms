package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 转账交易
 *
 * @author root
 */
@Component
@Scope("prototype")
public class Transfer implements Serializable {
    /**
     * 交易流水号
     */
    private String dealNo;
    /**
     * 交易日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dealDate;
    /**
     * 转账类型
     * 0 同行转账  1 跨行转账  2 跨境转账
     */
    private Integer transType;
    /**
     * 转账状态
     * 0处理中 1成功  2失败
     */
    private String transStatus;
    /**
     * 转出账号
     */
    private String accOut;
    /**
     * 转出账号用户名
     */
    private String accOutName;
    /**
     * 转出行
     */
    private String accOutBank;
    /**
     * 转入账号
     */
    private String accIn;
    /**
     * 转入账号用户名
     */
    private String accInName;
    /**
     * 转入行
     */
    private String accInBank;
    /**
     * 币种
     */
    private String currency;
    /**
     * 转账金额
     */
    private BigDecimal transFund;
    /**
     * 交易类型，就是说，交易属于转账、归集转账还是收款转账
     */
    private String kind;

    /**
     * 手续费
     */
    private BigDecimal fee;

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getDealNo() {
        return dealNo;
    }

    public void setDealNo(String dealNo) {
        this.dealNo = dealNo;
    }

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getAccOut() {
        return accOut;
    }

    public void setAccOut(String accOut) {
        this.accOut = accOut;
    }

    public String getAccOutName() {
        return accOutName;
    }

    public void setAccOutName(String accOutName) {
        this.accOutName = accOutName;
    }

    public String getAccOutBank() {
        return accOutBank;
    }

    public void setAccOutBank(String accOutBank) {
        this.accOutBank = accOutBank;
    }

    public String getAccIn() {
        return accIn;
    }

    public void setAccIn(String accIn) {
        this.accIn = accIn;
    }

    public String getAccInName() {
        return accInName;
    }

    public void setAccInName(String accInName) {
        this.accInName = accInName;
    }

    public String getAccInBank() {
        return accInBank;
    }

    public void setAccInBank(String accInBank) {
        this.accInBank = accInBank;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTransFund() {
        return transFund;
    }

    public void setTransFund(BigDecimal transFund) {
        this.transFund = transFund;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "dealNo='" + dealNo + '\'' +
                ", dealDate=" + dealDate +
                ", transType=" + transType +
                ", transStatus='" + transStatus + '\'' +
                ", accOut='" + accOut + '\'' +
                ", accOutName='" + accOutName + '\'' +
                ", accOutBank='" + accOutBank + '\'' +
                ", accIn='" + accIn + '\'' +
                ", accInName='" + accInName + '\'' +
                ", accInBank='" + accInBank + '\'' +
                ", currency='" + currency + '\'' +
                ", transFund=" + transFund +
                ", kind='" + kind + '\'' +
                ", fee=" + fee +
                '}';
    }
}
