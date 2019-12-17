package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author junqi
 * 催款通知
 */
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date infoTime;
}
