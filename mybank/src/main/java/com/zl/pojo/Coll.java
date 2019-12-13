package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 归集
 *
 * @author root
 */
@Component
@Scope("prototype")
public class Coll implements Serializable {
    /**
     * 签约单号
     */
    private String collId;
    /**
     * 主账号
     */
    private String mainAcc;
    /**
     *主账号持卡人
     */
    private String mainUser;
    /**
     * 主账号开户行
     */
    private String mainBank;
    /**
     * 子账号
     */
    private String followAcc;
    /**
     *子账号持卡人
     */
    private String followUser;
    /**
     * 子账号开户行
     */
    private String followBank;
    /**
     * 签约时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signDate;
    /**
     * 签约金额
     */
    private BigDecimal signFund;

    public String getCollId() {
        return collId;
    }

    public void setCollId(String collId) {
        this.collId = collId;
    }

    public String getMainAcc() {
        return mainAcc;
    }

    public void setMainAcc(String mainAcc) {
        this.mainAcc = mainAcc;
    }

    public String getFollowAcc() {
        return followAcc;
    }

    public void setFollowAcc(String followAcc) {
        this.followAcc = followAcc;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public BigDecimal getSignFund() {
        return signFund;
    }

    public void setSignFund(BigDecimal signFund) {
        this.signFund = signFund;
    }

    public String getMainUser() {
        return mainUser;
    }

    public void setMainUser(String mainUser) {
        this.mainUser = mainUser;
    }

    public String getMainBank() {
        return mainBank;
    }

    public void setMainBank(String mainBank) {
        this.mainBank = mainBank;
    }

    public String getFollowUser() {
        return followUser;
    }

    public void setFollowUser(String followUser) {
        this.followUser = followUser;
    }

    public String getFollowBank() {
        return followBank;
    }

    public void setFollowBank(String followBank) {
        this.followBank = followBank;
    }

    @Override
    public String toString() {
        return "Coll{" +
                "collId='" + collId + '\'' +
                ", mainAcc='" + mainAcc + '\'' +
                ", mainUser='" + mainUser + '\'' +
                ", mainBank='" + mainBank + '\'' +
                ", followAcc='" + followAcc + '\'' +
                ", followUser='" + followUser + '\'' +
                ", followBank='" + followBank + '\'' +
                ", signDate=" + signDate +
                ", signFund=" + signFund +
                '}';
    }
}
