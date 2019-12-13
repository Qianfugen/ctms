package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @author junqi
 */
@Component
public class Query implements Serializable {
    private String qname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String qAccNo;
    private String qType;
    private String qMainAccNo;
    private String qFollowAccNo;
    private String debtor;//贷方账号
    private String debtorName;//贷方姓名
    private String creditorAcc;//收款人账号

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getqAccNo() {
        return qAccNo;
    }

    public void setqAccNo(String qAccNo) {
        this.qAccNo = qAccNo;
    }

    public String getqType() {
        return qType;
    }

    public void setqType(String qType) {
        this.qType = qType;
    }

    public String getqMainAccNo() {
        return qMainAccNo;
    }

    public void setqMainAccNo(String qMainAccNo) {
        this.qMainAccNo = qMainAccNo;
    }

    public String getqFollowAccNo() {
        return qFollowAccNo;
    }

    public void setqFollowAccNo(String qFollowAccNo) {
        this.qFollowAccNo = qFollowAccNo;
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

    public String getCreditorAcc() {
        return creditorAcc;
    }

    public void setCreditorAcc(String creditorAcc) {
        this.creditorAcc = creditorAcc;
    }

    @Override
    public String toString() {
        return "Query{" +
                "qname='" + qname + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", qAccNo='" + qAccNo + '\'' +
                ", qType='" + qType + '\'' +
                ", qMainAccNo='" + qMainAccNo + '\'' +
                ", qFollowAccNo='" + qFollowAccNo + '\'' +
                ", debtor='" + debtor + '\'' +
                ", debtorName='" + debtorName + '\'' +
                ", creditorAcc='" + creditorAcc + '\'' +
                '}';
    }
}
