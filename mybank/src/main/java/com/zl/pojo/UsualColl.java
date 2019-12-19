package com.zl.pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author junqi
 * 常用收款人
 */
@Component
@Scope("prototype")
public class UsualColl implements Serializable {
    /**
     * 自己账号
     */
    private String mainAcc;
    /**
     * 收款人账号
     */
    private String accIn;
    /**
     * 收款人用户名
     */
    private String accInName;
    /**
     * 收款金额
     */
    private BigDecimal transFund;

    public String getMainAcc() {
        return mainAcc;
    }

    public void setMainAcc(String mainAcc) {
        this.mainAcc = mainAcc;
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

    public BigDecimal getTransFund() {
        return transFund;
    }

    public void setTransFund(BigDecimal transFund) {
        this.transFund = transFund;
    }

    @Override
    public String toString() {
        return "UsualColl{" +
                "mainAcc='" + mainAcc + '\'' +
                ", accIn='" + accIn + '\'' +
                ", accInName='" + accInName + '\'' +
                ", transFund=" + transFund +
                '}';
    }
}
