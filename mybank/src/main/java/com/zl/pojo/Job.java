package com.zl.pojo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

@Component
public class Job implements Serializable {
    /**
     * 任务序号
     */
    private Integer id;
    /**
     * 任务表达式
     */
    private String cron;
    /**
     * 转入账户
     */
    private String accIn;
    /**
     * 转出账户
     */
    private String accOut;
    /**
     * 转账金额
     */
    private BigDecimal transFund;

    /**
     * 币种
     */
    private String currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getAccIn() {
        return accIn;
    }

    public void setAccIn(String accIn) {
        this.accIn = accIn;
    }

    public String getAccOut() {
        return accOut;
    }

    public void setAccOut(String accOut) {
        this.accOut = accOut;
    }

    public BigDecimal getTransFund() {
        return transFund;
    }

    public void setTransFund(BigDecimal transFund) {
        this.transFund = transFund;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", cron='" + cron + '\'' +
                ", accIn='" + accIn + '\'' +
                ", accOut='" + accOut + '\'' +
                ", transFund='" + transFund + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
