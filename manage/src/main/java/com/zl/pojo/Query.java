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

    @Override
    public String toString() {
        return "Query{" +
                "qname='" + qname + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", qAccNo='" + qAccNo + '\'' +
                ", qType='" + qType + '\'' +
                '}';
    }

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
}
