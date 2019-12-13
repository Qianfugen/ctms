package com.zl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @author junqi
 * 登入记录表
 */
@Component
@Scope("prototype")
public class Login implements Serializable {
    /**
     * 账号
     */
    private String accNO;
    /**
     * 登入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date loginTime;
    /**
     * 登入地址
     */
    private String loginAdd;
    /**
     * 登入ip
     */
    private String loginIp;
    /**
     * 登入状态
     */
    private Integer loginCount;
    /**
     * 当天登入次数
     */
    private Integer loginError;
    /**
     * 登入异常原因
     */
    private String loginOdd;
}
