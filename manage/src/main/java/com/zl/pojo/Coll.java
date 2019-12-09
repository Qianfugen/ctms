package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 归集
 *
 * @author root
 */
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
     * 子账号
     */
    private String followAcc;
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

    @Override
    public String toString() {
        return "Coll{" +
                "collId='" + collId + '\'' +
                ", mainAcc='" + mainAcc + '\'' +
                ", followAcc='" + followAcc + '\'' +
                ", signDate=" + signDate +
                ", signFund=" + signFund +
                '}';
    }
}
